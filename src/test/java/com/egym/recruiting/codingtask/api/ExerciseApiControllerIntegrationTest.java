package com.egym.recruiting.codingtask.api;

import com.egym.recruiting.codingtask.model.Exercise;
import com.jayway.restassured.RestAssured;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.time.OffsetDateTime;

import static com.jayway.restassured.RestAssured.given;

/**
 * Integration test for testing the RESTful APIs provided by the {@link ExerciseApiController}. For easier testing we are going
 * to rely on the Rest Assured framework.
 */
@SpringBootTest
public class ExerciseApiControllerIntegrationTest {

	@LocalServerPort
	int localServerPort;

	@BeforeEach
	public void setUp() {
		RestAssured.port = localServerPort;
	}

	@Test
	public void testAddExercise() {
		Exercise ex1 = new Exercise();
		ex1.setUserId(1000L);
		ex1.setStartTime(OffsetDateTime.now());
		ex1.setDuration(60L);
		ex1.setCalories(5L);

		given().body(ex1).contentType("application/json").
				when().post("exercise").
				then().statusCode(201);
	}

	// TODO add more tests for the ExerciseApiController
}
