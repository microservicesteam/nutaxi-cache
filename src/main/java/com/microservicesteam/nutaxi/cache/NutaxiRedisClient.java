package com.microservicesteam.nutaxi.cache;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.apache.commons.lang3.Validate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.microservicesteam.nutaxi.cache.model.Route;

@SpringBootApplication
public class NutaxiRedisClient {

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

	@RestController
	public static class Controller {

		@RequestMapping(path = "/route", method = POST, consumes = "application/json", produces = "application/json")
		ResponseEntity<?> add(@RequestBody Route input) {

			Validate.notNull(input);
			Validate.isTrue(input.getId() == null);
			Validate.notNull(input.getOrigin());
			Validate.notNull(input.getDestination());

			input.setId("1");

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setLocation(ServletUriComponentsBuilder
					.fromCurrentRequest()
					.path("/{id}")
					.buildAndExpand(input.getId())
					.toUri());
			return new ResponseEntity<>(input, httpHeaders, CREATED);
		}

		@RequestMapping(path = "/route/{id}", method = GET)
		public Route route(@PathVariable(value = "id") String id) {
			return Route.builder()
					.id(id)
					.origin(randomAlphabetic(25))
					.destination(randomAlphabetic(25))
					.build();
		}

	}

}
