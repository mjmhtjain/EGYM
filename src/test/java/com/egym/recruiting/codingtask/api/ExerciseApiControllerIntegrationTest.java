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
    public void insertExercise_invalidStartEndDateOrCalories_respondWithStatusCode422() {
        ExerciseDTO ex1 = getDemoExerciseObj();
        ex1.setStartTime(OffsetDateTime.now().plusDays(1));

        given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post(baseUrl + "/exercise").
                then().statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());

        ex1 = getDemoExerciseObj();
        ex1.setCalories(Long.valueOf(-2));
        given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post(baseUrl + "/exercise").
                then().statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void insertExercise_badStartTimeFormat_respondWithStatusCode422() {
        ExerciseDTO ex1 = getDemoExerciseObj();
        ex1.setStartTime(OffsetDateTime.now().plusDays(1));

        given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post(baseUrl + "/exercise").
                then().statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void insertExercise_descriptionContainsSymbols_respondWithStatusCode422() {
        ExerciseDTO ex1 = getDemoExerciseObj();
        ex1.setStartTime(OffsetDateTime.now().plusDays(1));

        given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post(baseUrl + "/exercise").
                then().statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void insertExercise_conflictingStartTimeAndDuration_respondWithStatusCode422() {
        ExerciseDTO ex1 = getDemoExerciseObj();
        ex1.setStartTime(OffsetDateTime.now().plusDays(1));

        given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post(baseUrl + "/exercise").
                then().statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
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
    public void updateExercise_invalidExercise_respondWith400() {
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
    public void updateExercise_exerciseIdDoesNotExist_respondWith400() {
        ExerciseDTO ex1 = getDemoExerciseObj();
        Exercise insertExerciseResponse = given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post(baseUrl + "/exercise").
                then().statusCode(HttpStatus.CREATED.value())
                .extract().as(Exercise.class);

        String updatedDesc = "updatedDesc";
        long randomExerciseId = 12;
        ex1.setDescription(updatedDesc);
        given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("exerciseId", randomExerciseId)
                .when().put(baseUrl + "/exercise/{exerciseId}")
                .then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void updateExercise_updatingUserId_respondWith400() {
        ExerciseDTO ex1 = getDemoExerciseObj();
        Exercise insertExerciseResponse = given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(baseUrl + "/exercise")
                .then().statusCode(HttpStatus.CREATED.value())
                .extract().as(Exercise.class);

        ex1.setUserId(Long.valueOf(23));
        given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("exerciseId", insertExerciseResponse.getId())
                .when().put(baseUrl + "/exercise/{exerciseId}")
                .then().statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void updateExercise_updatingExerciseType_respondWith400() {
        ExerciseDTO ex1 = getDemoExerciseObj();
        Exercise insertExerciseResponse = given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(baseUrl + "/exercise")
                .then().statusCode(HttpStatus.CREATED.value())
                .extract().as(Exercise.class);

        ex1.setType(ExerciseType.SWIMMING);
        given().body(ex1).contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("exerciseId", insertExerciseResponse.getId())
                .when().put(baseUrl + "/exercise/{exerciseId}")
                .then().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    ExerciseDTO getDemoExerciseObj() {
        ExerciseDTO exerciseDTO = new ExerciseDTO();
        exerciseDTO.setUserId(Long.valueOf(1));
        exerciseDTO.setType(ExerciseType.RUNNING);
        exerciseDTO.setDescription("asd");
        exerciseDTO.setDuration(Long.valueOf(1));
        exerciseDTO.setCalories(Long.valueOf(1));
        exerciseDTO.setStartTime(OffsetDateTime.now());

        return exerciseDTO;
    }
}
