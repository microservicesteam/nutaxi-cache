package com.microservicesteam.nutaxi.cache.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.security.SecureRandom;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
public class RouteController {

    private static final SecureRandom RANDOM = new SecureRandom();

    @Autowired
    private RedisTemplate<Long, Route> routeTemplate;

    @RequestMapping(path = "/route", method = POST, consumes = "application/json", produces = "application/json")
    ResponseEntity<?> add(@RequestBody Route input) {
        return new ResponseEntity<>(cacheRoute(validate(input)), getHttpResponseHeaders(input), CREATED);
    }

    private static Route validate(Route input) {
        Validate.notNull(input);
        Validate.isTrue(input.getId() == null);
        Validate.notNull(input.getOrigin());
        Validate.notNull(input.getDestination());

        return input;
    }

    private Route cacheRoute(Route input) {
        Route route = Route.builder()
                .id(RANDOM.nextLong())
                .origin(input.getOrigin())
                .destination(input.getDestination())
                .build();

        routeTemplate.opsForValue().set(route.getId(), route);

        return route;
    }

    private static HttpHeaders getHttpResponseHeaders(Route input) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(input.getId())
                .toUri());
        return httpHeaders;
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
