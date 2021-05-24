package com.egym.recruiting.codingtask.api;

import com.egym.recruiting.codingtask.Application;
import com.egym.recruiting.codingtask.dao.ExerciseRepository;
import com.egym.recruiting.codingtask.dto.ExerciseDTO;
import com.egym.recruiting.codingtask.model.Exercise;
import com.egym.recruiting.codingtask.model.ExerciseType;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

/**
 * Integration test for testing the RESTful APIs provided by the {@link ExerciseApiController}. For easier testing we are going
 * to rely on the Rest Assured framework.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ExerciseApiControllerIntegrationTest {
    private final String baseUrl = "http://localhost:8080";

    @Autowired
    private ExerciseRepository exerciseRepository;

    @After
    public void tearDown() {
        exerciseRepository.deleteAllExercise();
    }

    @Test
    public void insertExercise_validExercise_respondWithStatusCode201() {
        ExerciseDTO ex1 = getDemoExerciseObj();
        given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post(baseUrl + "/exercise").
                then().statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void insertExercise_nullOrBlankParams_respondWithStatusCode400() {
        ExerciseDTO ex1 = getDemoExerciseObj();
        ex1.setDescription("");
        given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post(baseUrl + "/exercise").
                then().statusCode(HttpStatus.BAD_REQUEST.value());

        ex1 = getDemoExerciseObj();
        ex1.setDescription(null);
        given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post(baseUrl + "/exercise").
                then().statusCode(HttpStatus.BAD_REQUEST.value());

        ex1 = getDemoExerciseObj();
        ex1.setCalories(null);
        given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post(baseUrl + "/exercise").
                then().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void insertExercise_invalidParamValues_respondWithStatusCode422() {
        //invalid StartTime
        ExerciseDTO ex1 = getDemoExerciseObj();
        ex1.setStartTime(OffsetDateTime.now().plusDays(1));
        given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post(baseUrl + "/exercise").
                then().statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());

        //negative Calories
        ex1 = getDemoExerciseObj();
        ex1.setCalories(Long.valueOf(-2));
        given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post(baseUrl + "/exercise").
                then().statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());

        //negative Duration
        ex1 = getDemoExerciseObj();
        ex1.setDuration(Long.valueOf(-2));
        given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post(baseUrl + "/exercise").
                then().statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());

        //non-alphanumeric Description
        ex1 = getDemoExerciseObj();
        ex1.setDescription("!adsfasdf%");
        given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post(baseUrl + "/exercise").
                then().statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void insertExercise_conflictingStartTimeAndDuration_respondWithStatusCode409() {
        OffsetDateTime existingEntryStartTime = OffsetDateTime.now().minusDays(1);
        Long existingEntryDuration = Long.valueOf(120);

        //insert an exercise
        ExerciseDTO ex1 = getDemoExerciseObj();
        ex1.setStartTime(existingEntryStartTime);
        ex1.setDuration(existingEntryDuration);

        Exercise insertExerciseResponse = given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post(baseUrl + "/exercise").
                then().statusCode(HttpStatus.CREATED.value())
                .extract().as(Exercise.class);


        ex1 = getDemoExerciseObj();
        ex1.setStartTime(existingEntryStartTime.minusSeconds(30));
        ex1.setDuration(Long.valueOf(60));

        given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post(baseUrl + "/exercise").
                then().statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    public void insertExercise_exercisesWithDifferentTimeZonesConflicting_respondWithStatusCode409() {
        OffsetDateTime startTime;
        Long exerciseDuration = Long.valueOf(60);

        //invalid StartTime
        ExerciseDTO ex1 = getDemoExerciseObj();
        startTime = ex1.getStartTime();
        ex1.setStartTime(startTime.withOffsetSameInstant(ZoneOffset.ofHours(1)));
        ex1.setDuration(exerciseDuration);

        given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post(baseUrl + "/exercise").
                then().statusCode(HttpStatus.CREATED.value());

        ex1 = getDemoExerciseObj();
        ex1.setStartTime(startTime.withOffsetSameInstant(ZoneOffset.ofHours(2))
                .minusSeconds(5));
        ex1.setDuration(exerciseDuration);

        given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post(baseUrl + "/exercise").
                then().statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    public void updateExercise_validExercise_respondWith200CodeAndUpdatedEntity() {
        //insert an exercise
        ExerciseDTO ex1 = getDemoExerciseObj();
        Exercise insertExerciseResponse = given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post(baseUrl + "/exercise").
                then().statusCode(HttpStatus.CREATED.value())
                .extract().as(Exercise.class);

        //validate the update endpoint
        String updatedDesc = "updatedDesc";
        ex1.setDescription(updatedDesc);
        given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("exerciseId", insertExerciseResponse.getId())
                .when().put(baseUrl + "/exercise/{exerciseId}")
                .then().statusCode(HttpStatus.OK.value())
                .body("description", is(updatedDesc));
    }

    @Test
    public void updateExercise_invalidParamValues_respondWith400() {
        //insert an exercise
        ExerciseDTO ex1 = getDemoExerciseObj();
        Exercise insertExerciseResponse = given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post(baseUrl + "/exercise").
                then().statusCode(HttpStatus.CREATED.value())
                .extract().as(Exercise.class);

        //validate the update endpoint
        String updatedDesc = "";
        ex1.setDescription(updatedDesc);
        given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("exerciseId", insertExerciseResponse.getId())
                .when().put(baseUrl + "/exercise/{exerciseId}")
                .then().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateExercise_exerciseIdDoesNotExist_respondWith404() {
        ExerciseDTO ex1 = getDemoExerciseObj();
        Exercise insertExerciseResponse = given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post(baseUrl + "/exercise").
                then().statusCode(HttpStatus.CREATED.value())
                .extract().as(Exercise.class);

        String updatedDesc = "updatedDesc";
        long randomExerciseId = 12;
        insertExerciseResponse.setDescription(updatedDesc);
        given().body(insertExerciseResponse).contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("exerciseId", randomExerciseId)
                .when().put(baseUrl + "/exercise/{exerciseId}")
                .then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void updateExercise_updatingUserId_respondWith403() {
        ExerciseDTO ex1 = getDemoExerciseObj();
        Exercise insertExerciseResponse = given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(baseUrl + "/exercise")
                .then().statusCode(HttpStatus.CREATED.value())
                .extract().as(Exercise.class);

        insertExerciseResponse.setUserId(Long.valueOf(23));
        given().body(insertExerciseResponse).contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("exerciseId", insertExerciseResponse.getId())
                .when().put(baseUrl + "/exercise/{exerciseId}")
                .then().statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void updateExercise_updatingExerciseType_respondWith422() {
        ExerciseDTO ex1 = getDemoExerciseObj();
        Exercise insertExerciseResponse = given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(baseUrl + "/exercise")
                .then().statusCode(HttpStatus.CREATED.value())
                .extract().as(Exercise.class);

        insertExerciseResponse.setType(ExerciseType.SWIMMING);
        given().body(insertExerciseResponse).contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("exerciseId", insertExerciseResponse.getId())
                .when().put(baseUrl + "/exercise/{exerciseId}")
                .then().statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void updateExercise_updateExistingExerciseToConflictingTime_respondWith409() {
        //inserting two exercise entries
        ExerciseDTO ex = getDemoExerciseObj();
        Exercise exercise1 = given().body(ex).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(baseUrl + "/exercise")
                .then().statusCode(HttpStatus.CREATED.value())
                .extract().as(Exercise.class);

        ex = getDemoExerciseObj();
        ex.setStartTime(ex.getStartTime().plusHours(1));
        Exercise exercise2 = given().body(ex).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(baseUrl + "/exercise")
                .then().statusCode(HttpStatus.CREATED.value())
                .extract().as(Exercise.class);

        //updating exercise startTime to conflict with another
        exercise1.setStartTime(exercise2.getStartTime());
        given().body(exercise1).contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("exerciseId", exercise1.getId())
                .when().put(baseUrl + "/exercise/{exerciseId}")
                .then().statusCode(HttpStatus.CONFLICT.value());
    }

    ExerciseDTO getDemoExerciseObj() {
        ExerciseDTO exerciseDTO = new ExerciseDTO();
        exerciseDTO.setUserId(Long.valueOf(1));
        exerciseDTO.setType(ExerciseType.RUNNING);
        exerciseDTO.setDescription("asd");
        exerciseDTO.setDuration(Long.valueOf(1));
        exerciseDTO.setCalories(Long.valueOf(1));
        exerciseDTO.setStartTime(OffsetDateTime.now().minusDays(1));

        return exerciseDTO;
    }
}
