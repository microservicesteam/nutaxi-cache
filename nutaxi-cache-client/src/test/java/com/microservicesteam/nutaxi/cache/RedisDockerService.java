package com.microservicesteam.nutaxi.cache;

import static com.github.dockerjava.api.model.ExposedPort.tcp;
import static com.github.dockerjava.api.model.Ports.Binding.bindPort;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;

import org.apache.commons.lang.Validate;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DockerClientBuilder;
import com.google.common.base.Throwables;

public class RedisDockerService {

	private static final String IMAGE_NAME = "redis";
	private static final int DEFAULT_PORT = 6379;
	private static final Random RANDOM = new Random();
	private static final int SEED = 1000;

	private final DockerClient dockerClient;

	private final String containerId;

	public RedisDockerService(String containerName) {
		dockerClient = DockerClientBuilder.getInstance().build();
		containerId = createAndStartContainer(containerName);
	}

	public String createAndStartContainer(String containerName) {
		return startContainer(createContainer(containerName + "-" + RANDOM.nextInt(SEED)));
	}

	private String createContainer(String name) {
		Validate.isTrue(isAvailablePort(DEFAULT_PORT), "Port " + DEFAULT_PORT + " is not available");

		ExposedPort tcp6379 = tcp(DEFAULT_PORT);
		Ports portBindings = new Ports(tcp6379, bindPort(DEFAULT_PORT));

		return dockerClient.createContainerCmd(IMAGE_NAME)
				.withName(name)
				.withExposedPorts(tcp6379)
				.withPortBindings(portBindings)
				.exec()
				.getId();
	}

	private String startContainer(String id) {
		dockerClient.startContainerCmd(id).exec();
		return id;
	}

	public void close() {
		Validate.notNull(dockerClient, "Docker client must be present");
		Validate.notNull(containerId, "Container ID must be present");

		dockerClient.stopContainerCmd(containerId)
				.exec();

		try {
			dockerClient.close();
		} catch (IOException exception) {
			throw Throwables.propagate(exception);
		}
	}

	private static boolean isAvailablePort(int port) {
		try (Socket ignored = new Socket("localhost", port)) {
			return false;
		} catch (@SuppressWarnings("unused") IOException ignored) {
			return true;
		}
	}

}
