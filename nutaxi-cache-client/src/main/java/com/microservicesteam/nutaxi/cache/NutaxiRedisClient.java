package com.microservicesteam.nutaxi.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootApplication
public class NutaxiRedisClient implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(NutaxiRedisClient.class);

	@Autowired
	private StringRedisTemplate template;

	public static void main(String[] args) {
		SpringApplication.run(NutaxiRedisClient.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		testConnection();
	}

	private void testConnection() {
		ValueOperations<String, String> operations = getOperations();
		if (!template.hasKey(getKey())) {
			operations.set(getKey(), "nutaxi");
		}
		LOGGER.info("Found key {}, value={}", getKey(), operations.get(getKey()));
	}

	private static String getKey() {
		return "microservicesteam.redis.test";
	}

	private ValueOperations<String, String> getOperations() {
		return template.opsForValue();
	}

}
