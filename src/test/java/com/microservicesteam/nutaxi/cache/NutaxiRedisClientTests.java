package com.microservicesteam.nutaxi.cache;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NutaxiRedisClient.class)
public class NutaxiRedisClientTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void contextLoads() {
		assertNotNull(applicationContext);
	}

	@Configuration
	static class RedisTestConfiguration {

		@Bean
		public RedisSerializer<?> defaultRedisSerializer() {
			return mock(RedisSerializer.class);
		}

		@Bean
		public RedisConnectionFactory connectionFactory() {
			RedisConnectionFactory factory = mock(RedisConnectionFactory.class);
			RedisConnection connection = mock(RedisConnection.class);

			when(factory.getConnection()).thenReturn(connection);

			return factory;
		}
	}

}
