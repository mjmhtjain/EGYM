package com.egym.recruiting.codingtask.api;

import static com.jayway.restassured.RestAssured.*;

import java.time.OffsetDateTime;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.egym.recruiting.codingtask.model.Exercise;
import com.jayway.restassured.RestAssured;

/**
 * Integration test for testing the RESTful APIs provided by the {@link ExerciseApiController}. For easier testing we are going
 * to rely on the Rest Assured framework.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
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
