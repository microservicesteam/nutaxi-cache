package com.microservicesteam.nutaxi.cache.controller.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = NOT_FOUND, reason = "No such route")
public class RouteNotFoundException extends RuntimeException {
}