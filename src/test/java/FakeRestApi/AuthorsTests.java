package FakeRestApi;

import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.ResponseAwareMatcher.*;
import static org.hamcrest.Matchers.*;
public class AuthorsTests {
    RequestSpecification request;
    @BeforeClass
    public void setup() {
        request = given()
                .log().all()
                .baseUri("https://fakerestapi.azurewebsites.net");
    }
    @Test
    public void testFetchAllAuthors() {
        // Send GET request to fetch all authors and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
        .when()
                .get("/api/v1/Authors")
        .then()
                .assertThat()
                .statusCode(200)  // Assert that the status code is 200 OK
                .body("size()", greaterThan(0))  // Assert that the response contains a list of authors
                .log().all();  // Optionally log the response for debugging
    }
    @Test
    public void testFetchAuthorByValidId() {
        // Valid Author ID
        int authorId = 1;

        // Send GET request to fetch author by valid ID and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
        .when()
                .get("/api/v1/Authors/" + authorId)
        .then()
                .assertThat()
                .statusCode(200)  // Assert that the status code is 200 OK
                .body("id", equalTo(authorId))  // Assert that the returned author ID matches the requested ID
                .log().all();  // Optionally log the response for debugging
    }
    @Test
    public void testCreateNewAuthor() {
        // Valid payload for creating a new author
        String newAuthorPayload = "{\n" +
                "  \"id\": 3,\n" +
                "  \"idBook\": 3,\n" +
                "  \"firstName\": \"ali\",\n" +
                "  \"lastName\": \"ihab\"\n" +
                "}";

        // Send POST request to create a new author and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
                .body(newAuthorPayload)  // Attach the new author payload
        .when()
                .post("/api/v1/Authors")
        .then()
                .assertThat()
                .statusCode(200)  // Assert that the status code is 200 Created
                .body("firstName", equalTo("ali"))  // Assert that the first name matches
                .body("lastName", equalTo("ihab"))  // Assert that the last name matches
                .log().all();  // Optionally log the response for debugging
    }
    @Test
    public void testUpdateAuthorByValidId() {
        // Valid Author ID
        int authorId = 1;
        // Payload for updating the author
        String updatedAuthorPayload = "{\n" +
                "  \"firstName\": \"Updated\",\n" +
                "  \"lastName\": \"Doe\"\n" +
                "}";

        // Send PUT request to update the author and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
                .body(updatedAuthorPayload)  // Attach the updated author payload
        .when()
                .put("/api/v1/Authors/" + authorId)
        .then()
                .assertThat()
                .statusCode(200)  // Assert that the status code is 200 OK
                .body("firstName", equalTo("Updated"))  // Assert that the first name has been updated
                .body("lastName", equalTo("Doe"))  // Assert that the last name has been updated
                .log().all();  // Optionally log the response for debugging
    }
    @Test
    public void testDeleteAuthorByValidId() {
        // Valid Author ID
        int authorId = 1;

        // Send DELETE request to delete the author and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
        .when()
                .delete("/api/v1/Authors/" + authorId)
        .then()
                .assertThat()
                .statusCode(200)  // Assert that the status code is 200 OK
                .body(equalTo(""));  // Assert that the response body is empty (assuming successful deletion returns an empty body)
        // .log().all();  // Optionally log the response for debugging
    }
    @Test
    public void testFetchAuthorByInvalidId() {
        // Invalid Author ID
        String invalidAuthorId = "abc";

        // Send GET request to fetch author by invalid ID and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
        .when()
                .get("/api/v1/Authors/" + invalidAuthorId)
        .then()
                .assertThat()
                .statusCode(400);  // Assert that the status code is 400 Bad Request
    }
    @Test
    public void testCreateAuthorWithMissingFields() {

        // Payload for creating an author with missing firstName
        String authorPayload = "{\n" +
                "  \"idBook\": 3,\n" +
                "  \"lastName\": \"ihab\"\n" +
                "}";

        // Send POST request to create an author and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
                .body(authorPayload)  // Attach the payload with missing fields
        .when()
                .post("/api/v1/Authors")
        .then()
                .assertThat()
                .statusCode(400)  // Assert that the status code is 400 Bad Request
                .body("message", containsString("Error"));
        // .log().all();  // Optionally log the response for debugging
    }
    @Test
    public void testCreateAuthorWithInvalidId() {

        // Payload for creating an author with invalid idBook
        String authorPayload = "{\n" +
                "  \"idBook\": \"invalid-id\",\n" +
                "  \"firstName\": \"Jane\",\n" +
                "  \"lastName\": \"Doe\"\n" +
                "}";

        // Send POST request to create an author and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
                .body(authorPayload)  // Attach the payload with invalid idBook
        .when()
                .post("/api/v1/Authors")
        .then()
                .assertThat()
                .statusCode(400);  // Assert that the status code is 400 Bad Request
    }
    @Test
    public void testUpdateAuthorWithEmptyPayload() {


        // Send PUT request to update author with an empty payload and validate the response
        given()
                .spec(request)

        .when()
                .put("/api/v1/Authors/1")
        .then()
                .assertThat()
                .statusCode(415) ; // Assert that the status code is 415 Unsupported Media Type

        // .log().all();  // Optionally log the response for debugging
    }
    @Test
    public void testUpdateAuthorWithInvalidData() {

        // Payload with invalid data type for firstName
        String invalidPayload = "{\n" +
                "  \"firstName\": 1234,\n" +
                "  \"lastName\": \"Doe\"\n" +
                "}";

        // Send PUT request to update the author and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
                .body(invalidPayload)  // Attach the payload with invalid data type
        .when()
                .put("/api/v1/Authors/1")
        .then()
                .assertThat()
                .statusCode(400) ; // Assert that the status code is 400 Bad Request
        // .log().all();  // Optionally log the response for debugging
    }
    @Test
    public void testDeleteAuthorWithInvalidId() {
        // Send DELETE request to delete an author with an invalid ID and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
        .when()
                .delete("/api/v1/Authors/9999")  // Invalid author ID
        .then()
                .assertThat()
                .statusCode(404) ; // Assert that the status code is 404 Not Found
        // .log().all();  // Optionally log the response for debugging
    }
    @Test
    public void testFetchAuthorsWithQueryParams() {
        // Send GET request with query parameters and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
                .queryParam("lastName", "Doe")  // Query parameter for last name
        .when()
                .get("/api/v1/Authors")  // Endpoint for fetching authors
        .then()
                .assertThat()
                .statusCode(200)  // Assert that the status code is 200 OK
                .body("lastName", everyItem(equalTo("Doe")));  // Assert that all returned authors have last name "Doe"
        // .log().all();  // Optionally log the response for debugging
    }
    @Test
    public void testFetchAuthorsWithUnsupportedMediaType() {

        // Send GET request with unsupported media type (application/xml) and validate the response
        given()
                .spec(request)
                .header("Accept", "application/xml")  // Unsupported media type
        .when()
                .get("/api/v1/Authors")  // Endpoint to fetch authors
        .then()
                .assertThat()
                .statusCode(415)  // Assert that the status code is 415 Unsupported Media Type
                .body("message", containsString("Unsupported Media Type"));  // Optionally check for an error message
        // .log().all();  // Optionally log the response for debugging
    }
}
