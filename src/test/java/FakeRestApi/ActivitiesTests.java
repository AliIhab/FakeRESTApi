package FakeRestApi;

import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.ResponseAwareMatcher.*;
import static org.hamcrest.Matchers.*;

public class ActivitiesTests {

    RequestSpecification request;
    @BeforeClass
    public void setup() {
        request = given()
                .log().all()
                .baseUri("https://fakerestapi.azurewebsites.net");


    }
    @Test
    public void testFetchAllActivities() {
        // Send GET request and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
        .when()
                .get("/api/v1/Activities")
        .then()
                .assertThat()
                .statusCode(200)  // Assert status code is 200
                .body("id", not(empty()))  // Assert that "id" is not empty in the response body
                .body("title", not(empty()));  // Assert that "title" is not empty in the response body
    }
    @Test
    public void testFetchActivityByValidId() {

        // Valid Activity ID to fetch
        int activityId = 2;

        // Send GET request and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
        .when()
                .get("/api/v1/Activities/" + activityId)
                .then()
                .assertThat()
                .statusCode(200)  // Assert status code is 200 OK
                .body("id", equalTo(activityId))  // Assert that the activity ID in the response matches the requested ID
                .body("title", not(emptyString()))  // Assert that the title is not empty
                .body("dueDate", not(emptyString()))  // Assert that dueDate is present
                .body("completed", notNullValue());  // Assert that the "completed" field is not null
    }
    @Test
    public void testCreateNewActivity() {

        // Request payload for creating a new activity
        String requestBody ="{ \"title\": \"New Activity\", \"description\": \"Activity Description\" }";

        // Send POST request and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
                .body(requestBody)

        .when()
                .post("/api/v1/Activities")
        .then()
                .assertThat()
                .statusCode(200)
                .log().all()
                .body("id", notNullValue())  // Assert that the response contains a non-null activity ID
                .body("title", equalTo("New Activity"))  // Assert that the title matches the one in the request
                .body("dueDate", notNullValue())  // Assert that the due date is generated and not null
                .body("completed", equalTo(false));  // Assert that the "completed" field defaults to false
    }
    @Test
    public void testUpdateActivityByValidId() {

        // Request payload for updating an activity
        String requestBody = "{\n" +
                "  \"title\": \"Updated Activity Title\"\n" +
                "}";

        // Valid Activity ID to update
        int activityId = 0;

        // Send PUT request and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
                .body(requestBody)  // Attach the request body
        .when()
                .put("/api/v1/Activities/" + activityId)
        .then()
                .assertThat()
                .statusCode(200)  // Assert that the status code is 200 OK
                .body("id", equalTo(activityId))  // Assert that the updated activity ID matches the requested ID
                .body("title", equalTo("Updated Activity Title"))  // Assert that the title is updated correctly
                .body("dueDate", notNullValue())  // Assert that the due date is still present
                .body("completed", notNullValue());  // Assert that the "completed" field remains unchanged
    }
    @Test
    public void testDeleteActivityByValidId() {

        // Valid Activity ID to delete
        int activityId = 1;

        // Send DELETE request and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
        .when()
                .delete("/api/v1/Activities/" + activityId)
        .then()
                .assertThat()
                .statusCode(200)  // Assert that the status code is 200 OK
                .body(equalTo(""));  // Assert that the response body is empty after deletion
    }
    @Test
    public void testFetchActivityByInvalidId() {
        // Invalid Activity ID
        String invalidActivityId = "abc";

        // Send GET request with an invalid ID and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
        .when()
                .get("/api/v1/Activities/" + invalidActivityId)
        .then()
                .assertThat()
                .statusCode(400)  // Assert that the status code is 400 Bad Request
                .body(containsString("not valid"));  // Optionally, assert that the response contains an error message
    }
    @Test
    public void testCreateActivityWithMissingFields() {
        // Request payload with missing title
        String requestBody = "{\n" +
                "  \"description\": \"Activity Description\",\n" +
                "}";

        // Send POST request with incomplete data and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
                .body(requestBody)  // Attach the request body
        .when()
                .post("/api/v1/Activities")
        .then()
                .assertThat()
                .statusCode(400)  // Assert that the status code is 400 Bad Request
                .body(containsString("Bad Request"));
    }
    @Test
    public void testUpdateActivityWithEmptyPayload() {
        // Valid Activity ID to update
        int activityId = 1;


        // Send PUT request with an empty payload and validate the response
        given()
                .spec(request)
        .when()
                .put("/api/v1/Activities/" + activityId)
        .then()
                .assertThat()
                .statusCode(415);  // Assert that the status code is 415 Unsupported Media Type
    }
    @Test
    public void testDeleteActivityWithInvalidId() {
        // Invalid Activity ID
        int invalidActivityId = 9999;

        // Send DELETE request for invalid ID and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
        .when()
                .delete("/api/v1/Activities/" + invalidActivityId)
        .then()
                .assertThat()
                .statusCode(404)  // Assert that the status code is 404 Not Found
                .body(containsString("Not Found"));  // Optionally, assert that the response contains "Not Found" message
    }
    @Test
    public void testCreateActivityWithInvalidData() {

        // Invalid payload
        String invalidPayload = "{\n" +
                "  \"invalidField\": \"Invalid Data\"\n" +
                "}";

        // Send POST request with invalid payload and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
                .body(invalidPayload)  // Attach the invalid payload
                .when()
                .post("/api/v1/Activities")
                .then()
                .assertThat()
                .statusCode(400)  // Assert that the status code is 400 Bad Request
                .body(containsString("Bad Request"));
    }
    @Test
    public void testFetchActivitiesWithQueryParams() {

        // Query parameter for filtering by date
        String queryDate = "2024-09-15";

        // Send GET request with query parameters and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
                .queryParam("date", queryDate)  // Attach the query parameter
        .when()
                .get("/api/v1/Activities")
        .then()
                .assertThat()
                .statusCode(200)  // Assert that the status code is 200 OK
                .body("date", everyItem(equalTo(queryDate)))  // Validate that all returned activities have the correct date
                .log().all();  // Optionally log the response for debugging
    }
    @Test
    public void testFetchActivitiesWithPagination() {
        // Pagination parameters
        int page = 1;
        int size = 30;

        // Send GET request with pagination parameters and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
                .queryParam("page", page)  // Attach the page query parameter
                .queryParam("size", size)  // Attach the size query parameter
        .when()
                .get("/api/v1/Activities")
        .then()
                .assertThat()
                .statusCode(200)  // Assert that the status code is 200 OK
                .body("size()", equalTo(size))  // Validate that the number of activities returned matches the size
                .log().all();  // Optionally log the response for debugging
    }

}

