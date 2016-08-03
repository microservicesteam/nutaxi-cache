package com.microservicesteam.nutaxi.cache;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import redis.embedded.RedisServer;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NutaxiRedisClient.class)
@WebAppConfiguration
public class NutaxiRedisClientTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private static RedisServer redisServer;

	private MockMvc mockMvc;

	@BeforeClass
	public static void init() throws Exception {
		redisServer = new RedisServer(6379);
		redisServer.start();
	}

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@After
	public void tearDown() throws Exception {
		redisServer.stop();
	}

	@Test
	public void contextLoads() {
		assertThat(applicationContext).isNotNull();
	}

	@Test
	public void routeCanBeRetrievedAfterSave() throws Exception {
		this.mockMvc.perform(post("/route")
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.content("{\"id\":\"1\",\"origin\":\"WhESYuyGbIfRghNlhgGmjQslS\",\"destination\":\"VTiCuGymAmdrhjapLtcKcXApf\"}"))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value("1"));

		this.mockMvc.perform(get("/route/1"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value("1"));
	}

}
