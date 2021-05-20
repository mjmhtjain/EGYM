package com.egym.recruiting.codingtask.api;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import com.jayway.restassured.RestAssured;

/**
 * Integration test for testing the RESTful APIs provided by the {@link ExerciseApiController}. For easier testing we are going
 * to rely on the Rest Assured framework.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RankingApiControllerIntegrationTest {

	@LocalServerPort
	int localServerPort;

	@BeforeEach
	public void setUp() {
		RestAssured.port = localServerPort;
	}

	@Test
	public void testGetRanking() {
		when().get("/ranking?userIds=1&userIds=2").
				then().statusCode(HttpStatus.SC_OK).body("", Matchers.hasSize(0));
	}

	// TODO add more tests for the RankingApiController
}
