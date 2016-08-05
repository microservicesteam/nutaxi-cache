package com.microservicesteam.nutaxi.cache;

import static com.google.common.base.Throwables.propagate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicesteam.nutaxi.cache.model.Route;

import redis.embedded.RedisServer;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = NutaxiRedisClient.class)
@WebAppConfiguration
public class NutaxiRedisClientTests {

    private static final SecureRandom RANDOM = new SecureRandom();

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private static RedisServer redisServer;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeClass
    public static void init() throws Exception {
        redisServer = new RedisServer(6379);
        redisServer.start();
    }

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        redisServer.stop();
    }

    @Test
    public void contextLoads() {
        assertThat(applicationContext).isNotNull();
    }

    @Test
    public void routeCanBeRetrievedAfterSave() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/route")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(getSampleContent()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        Route savedRoute = objectMapper.readValue(result.getResponse().getContentAsString(), Route.class);

        this.mockMvc.perform(get("/route/" + savedRoute.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedRoute.getId()));
    }

    private static String getSampleContent() {
        try {
            return new String(Files.readAllBytes(Paths.get("src/test/resources/examples/sample.json")), "UTF-8");
        } catch (IOException exception) {
            throw propagate(exception);
        }
    }

    @Test
    public void routeCannotBeRetrievedWithoutSave() throws Exception {
        this.mockMvc.perform(get("/route/" + RANDOM.nextInt()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
