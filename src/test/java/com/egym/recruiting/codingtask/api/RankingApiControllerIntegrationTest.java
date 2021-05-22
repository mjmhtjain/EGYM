package com.egym.recruiting.codingtask.api;

import com.egym.recruiting.codingtask.Application;
import com.egym.recruiting.codingtask.dto.ExerciseDTO;
import com.egym.recruiting.codingtask.model.ExerciseType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;

/**
 * Integration test for testing the RESTful APIs provided by the {@link ExerciseApiController}. For easier testing we are going
 * to rely on the Rest Assured framework.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RankingApiControllerIntegrationTest {
    private final String baseUrl = "http://localhost:8080";

    @Test
    public void testGetRanking() {
        long userId = 1;
        //insert a bunch of entries
        insertExercises(1, 10);
        insertExercises(2, 10);

        //assert ranking endpoint
        given().queryParameters(Map.of("userIds","1,2"))
                .when().get(baseUrl + "/ranking")
                .then().statusCode(HttpStatus.SC_OK)
                .body("", Matchers.hasSize(2));
    }

    void insertExercises(long userId, long noOfEntries) {
        for (long i = 0; i < noOfEntries; i++) {
            ExerciseDTO ex1 = getDemoExerciseObj(userId, i);
            given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE).
                    when().post(baseUrl + "/exercise").
                    then().statusCode(HttpStatus.SC_CREATED);
        }
    }

    private ExerciseDTO getDemoExerciseObj(long userId, long daysAgo) {
        ExerciseDTO exerciseDTO = new ExerciseDTO();
        exerciseDTO.setUserId(Long.valueOf(userId));
        exerciseDTO.setType(ExerciseType.RUNNING);
        exerciseDTO.setDescription("RUNNING");
        exerciseDTO.setDuration(Long.valueOf(80));
        exerciseDTO.setCalories(Long.valueOf(80));
        exerciseDTO.setStartTime(OffsetDateTime.now().minus(daysAgo, ChronoUnit.DAYS));

        return exerciseDTO;
    }

    // TODO add more tests for the RankingApiController
}
