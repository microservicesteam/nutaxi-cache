package com.microservicesteam.nutaxi.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

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

}
