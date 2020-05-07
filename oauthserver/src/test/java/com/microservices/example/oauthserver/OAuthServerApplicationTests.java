package com.microservices.example.oauthserver;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@Sql({ "/data-test.sql" })
class OAuthServerApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	private static EntityManagerFactory entityManagerFactory;

	@Autowired
	private void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		OAuthServerApplicationTests.entityManagerFactory = entityManagerFactory;
	}

	@Test
	protected void obtainAccessTokenTest() throws Exception {
		ResultActions result = mockMvc
				.perform(post("/oauth/token").param("grant_type", "password").param("scope", "profile greet upload")
						.param("client_id", "test_clientid").param("client_secret", "myclientsecret")
						.param("username", "test_admin").param("password", "pass")
						.accept("application/www-form-encoded"))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));
		String resultString = result.andReturn().getResponse().getContentAsString();

		JacksonJsonParser jsonParser = new JacksonJsonParser();
		assertNotNull(jsonParser.parseMap(resultString).get("access_token"));
	}

	@AfterAll
	protected static void tearDown() {
		EntityManager session = null;
		EntityTransaction transaction = null;
		try {
			session = entityManagerFactory.createEntityManager();
			transaction = session.getTransaction();
			transaction.begin();
			session.createNativeQuery("delete from authorities where username like 'test_%'").executeUpdate();
			session.createNativeQuery("delete from users where username like 'test_%'").executeUpdate();
			session.createNativeQuery("delete from oauth_client_details where client_id like 'test_%'").executeUpdate();
			session.createNativeQuery("delete from oauth_refresh_token where token_id in ("
					+ "select refresh_token from oauth_access_token where client_id like 'test_%'" + ")")
					.executeUpdate();
			session.createNativeQuery("delete from oauth_access_token where client_id like 'test_%'").executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			fail(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}

}
