package com.microservicesteam.nutaxi.cache;

import static org.junit.Assert.assertNotNull;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NutaxiRedisClient.class)
public class NutaxiRedisClientTests {

	@Autowired
	private ApplicationContext applicationContext;

	private static RedisDockerService redisDockerService;

	@BeforeClass
	public static void setUpBeforeClass() {
		redisDockerService = new RedisDockerService();
		redisDockerService.createAndStartContainer("nutaxi-test-redis");
	}

	@AfterClass
	public static void tearDownAfterClass() {
		redisDockerService.close();
	}

	@Test
	public void contextLoads() {
		assertNotNull(applicationContext);
	}

}
