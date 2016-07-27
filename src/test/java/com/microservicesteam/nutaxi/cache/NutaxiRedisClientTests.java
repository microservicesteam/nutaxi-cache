package com.microservicesteam.nutaxi.cache;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.embedded.RedisServer;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NutaxiRedisClient.class)
public class NutaxiRedisClientTests {

	@Autowired
	private ApplicationContext applicationContext;

	private static RedisServer redisServer;

	@BeforeClass
	public static void init() throws Exception {
		redisServer = new RedisServer(6379);
		redisServer.start();
	}

	@After
	public void tearDown() throws Exception {
		redisServer.stop();
	}

	@Test
	public void contextLoads() {
		assertNotNull(applicationContext);
	}

}
