package com.microservicesteam.nutaxi.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.microservicesteam.nutaxi.cache.model.Route;

@SpringBootApplication
public class NutaxiRedisClient implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(NutaxiRedisClient.class);

	@Bean
	public RedisConnectionFactory connectionFactory() {
		return new JedisConnectionFactory();
	}

	@Bean
	public RedisTemplate<String, Route> template() {
		RedisTemplate<String, Route> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory());

		return template;
	}

	public static void main(String[] args) {
		SpringApplication.run(NutaxiRedisClient.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		testConnection();
	}

	private void testConnection() {
		ValueOperations<String, Route> operations = getOperations();
		if (!template().hasKey(getKey())) {
			operations.set(getKey(), getDummyRoute());
		}
		LOGGER.info("Found key {}, value={}", getKey(), operations.get(getKey()));
	}

	private static Route getDummyRoute() {
		return Route.builder()
				.id("12345")
				.origin("Szeged")
				.destination("Budapest")
				.build();
	}

	private static String getKey() {
		return "microservicesteam.redis.test";
	}

	private ValueOperations<String, Route> getOperations() {
		return template().opsForValue();
	}

}
