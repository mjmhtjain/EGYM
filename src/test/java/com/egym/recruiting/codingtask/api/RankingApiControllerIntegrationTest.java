package com.egym.recruiting.codingtask.api;

import com.egym.recruiting.codingtask.Application;
import com.egym.recruiting.codingtask.dto.ExerciseDTO;
import com.egym.recruiting.codingtask.model.ExerciseType;
import com.egym.recruiting.codingtask.model.RankingUser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.jayway.restassured.RestAssured.given;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

/**
 * Integration test for testing the RESTful APIs provided by the {@link ExerciseApiController}. For easier testing we are going
 * to rely on the Rest Assured framework.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RankingApiControllerIntegrationTest {
    private static final String TIMESTAMP_OFFSET_PROPERTY = "_timestampOffset";
    private static final String CONTENT_TYPE_JSON = MediaType.APPLICATION_JSON_VALUE;
    private final String baseUrl = "http://localhost:8080";
    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void testGetRanking() {
        List<String> testsFromFolder = readTestsFromFolder();
        for (String testFile : testsFromFolder) {
            runTests(testFile);
        }
    }

    private void runTests(String testFile) {
        System.out.println("-----Executing integration testcases from file " + testFile + "-----");
        List<String> jsonCases = readJsonCases(testFile);
        jsonCases.forEach(jsonCase -> {
            JsonNode testCase = parseJson(jsonCase);
            executeTestRequest(testCase);
        });
        return;
    }

    private void executeTestRequest(JsonNode testCase) {
        JsonNode request = testCase.get("request");
        JsonNode response = testCase.get("response");

        String method = request.get("method").asText();
        String url = request.get("url").asText();
        String requestBody = extractBody(request.get("body"));
        int statusCode = response.get("status_code").asInt();
        JsonNode responseBody = response.get("body");

        String contentType = null;
        String type = request.get("headers").get("Content-Type").asText();

        if (type.equals("application/json")) {
            contentType = CONTENT_TYPE_JSON;
        }

        switch (method) {
            case "POST": {
                try {
                    given().body(requestBody).contentType(contentType).
                            when().post(baseUrl + url).
                            then().statusCode(statusCode);
                } catch (Exception ex) {
                    throw new Error(ex);
                }

                break;
            }
            case "GET": {
                Map<String, ?> queryParams = extractQueryParams(request);
                try {
                    RankingUser[] actualResponse = given().contentType(contentType)
                            .queryParameters(queryParams)
                            .when().get(baseUrl + url)
                            .then().statusCode(statusCode)
                            .extract().body().as(RankingUser[].class);

                    RankingUser expectedResponse = OBJECT_MAPPER.treeToValue(responseBody, RankingUser.class);
                    assertEquals(actualResponse[0], expectedResponse);
                } catch (Exception ex) {
                    throw new Error(ex);
                }

                break;
            }
            default:
                break;
        }
    }

    private Map<String, ?> extractQueryParams(JsonNode request) {
        List<String> userIds = new ArrayList<>();

        request.get("queryParams").iterator()
                .forEachRemaining(item -> {
                    userIds.add(item.get("userIds").asText());
                });

        String delimitedParamValues = userIds.stream().collect(Collectors.joining(","));
        return Map.of("userIds", delimitedParamValues);
    }

    private String extractBody(JsonNode body) {
        if (!body.isObject()) {
            return body.toString();
        }

        ObjectNode bodyObject = (ObjectNode) body;

        if (bodyObject.has(TIMESTAMP_OFFSET_PROPERTY)) {
            long offset = bodyObject.get(TIMESTAMP_OFFSET_PROPERTY).asLong();
            bodyObject.remove(TIMESTAMP_OFFSET_PROPERTY);
            OffsetDateTime timestamp = OffsetDateTime.now().plusDays(offset);

            bodyObject.put("startTime", String.valueOf(timestamp));
        }
        return bodyObject.toString();
    }

    private JsonNode parseJson(String jsonString) {
        try {
            return OBJECT_MAPPER.readTree(jsonString);
        } catch (IOException ex) {
            throw new Error(ex);
        }
    }

    private List<String> readJsonCases(String filename) {
        ClassPathResource jsonResource = new ClassPathResource("testcases/" + filename);
        try (InputStream inputStream = jsonResource.getInputStream()) {
            return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(toList());
        } catch (IOException ex) {
            throw new Error(ex);
        }
    }

    private List<String> readTestsFromFolder() {
        try {
            return Files.list(Paths.get("src/test/resources/testcases"))
                    .filter(Files::isRegularFile)
                    .map(f -> f.getFileName().toString())
                    .filter(f -> f.endsWith(".json"))
                    .collect(toList());
        } catch (IOException ex) {
            throw new Error(ex);
        }
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
