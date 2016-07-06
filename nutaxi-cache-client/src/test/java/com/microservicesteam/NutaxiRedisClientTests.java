package com.microservicesteam;

import static com.google.common.base.Throwables.propagate;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Random;

import org.apache.commons.lang.Validate;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DockerClientBuilder;

// TODO szogibalu MS-32  some polishing
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NutaxiRedisClient.class)
public class NutaxiRedisClientTests {

	@Autowired
	private ApplicationContext applicationContext;

	private static DockerClient dockerClient;

	private static String containerId;

	@BeforeClass
	public static void init() {
		dockerClient = DockerClientBuilder.getInstance().build();

		ExposedPort tcp6379 = ExposedPort.tcp(6379);

		Ports portBindings = new Ports();
		portBindings.bind(tcp6379, Ports.Binding.bindPort(6379));

		String name = "nutaxi-redis-" + new Random().nextInt();
		CreateContainerResponse container = dockerClient.createContainerCmd("redis").withName(name)
				.withExposedPorts(tcp6379, tcp6379).withPortBindings(portBindings).exec();

		containerId = container.getId();

		dockerClient.startContainerCmd(containerId).exec();
	}

	@AfterClass
	public static void tearDown() {
		Validate.notNull(containerId, "Container ID must be present");

		dockerClient.stopContainerCmd(containerId).exec();
		try {
			dockerClient.close();
		} catch (IOException exception) {
			throw propagate(exception);
		}
	}

	@Test
	public void contextLoads() {
		assertNotNull(applicationContext);
	}

}
