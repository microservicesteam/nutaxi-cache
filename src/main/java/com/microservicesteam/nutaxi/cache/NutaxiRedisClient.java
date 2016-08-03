package com.microservicesteam.nutaxi.cache;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.security.SecureRandom;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
	public RedisTemplate<Long, Route> template() {
		RedisTemplate<Long, Route> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory());

		return template;
	}

	public static void main(String[] args) {
		SpringApplication.run(NutaxiRedisClient.class, args);
	}

	@RestController
	public static class Controller {

		private static final SecureRandom RANDOM = new SecureRandom();

		@Autowired
		private RedisTemplate<Long, Route> routeTemplate;

		@RequestMapping(path = "/route", method = POST, consumes = "application/json", produces = "application/json")
		ResponseEntity<?> add(@RequestBody Route input) {

			Validate.notNull(input);
			Validate.isTrue(input.getId() == null);
			Validate.notNull(input.getOrigin());
			Validate.notNull(input.getDestination());

			input.setId(RANDOM.nextLong());
			routeTemplate.opsForValue().set(input.getId(), input);

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setLocation(ServletUriComponentsBuilder
					.fromCurrentRequest()
					.path("/{id}")
					.buildAndExpand(input.getId())
					.toUri());
			return new ResponseEntity<>(input, httpHeaders, CREATED);
		}

		@RequestMapping(path = "/route/{id}", method = GET)
		public Route route(@PathVariable(value = "id") Long id) {
			Validate.notNull(id);
			ValueOperations<Long, Route> operations = routeTemplate.opsForValue();

			if (!routeTemplate.hasKey(id)) {
				throw new RouteNotFoundException();
			}

			return operations.get(id);
		}

		@ResponseStatus(value = NOT_FOUND, reason = "No such route")
		public class RouteNotFoundException extends RuntimeException {
		}

	}

}
