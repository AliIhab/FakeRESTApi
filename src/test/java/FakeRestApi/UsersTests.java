package FakeRestApi;

import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.ResponseAwareMatcher.*;
import static org.hamcrest.Matchers.*;
public class UsersTests {
    RequestSpecification request;
    @BeforeClass
    public void setup() {
        request = given()
                .log().all()
                .baseUri("https://fakerestapi.azurewebsites.net");
    }
    @Test
    public void fetchAllUsers() {

        // Send GET request to fetch all users and verify the response
        given()
                .spec(request)
        .when()
                .get("/api/v1/Users") // GET request to retrieve all users
        .then()
                .statusCode(200) // Verify status code is 200 OK
                .body("size()", greaterThan(0)); // Verify the list is not empty
    }
    @Test
    public void fetchUserById() {

        // Send GET request to fetch user by valid ID and verify the response
        given()
                .spec(request)
        .when()
                .get("/api/v1/Users/1") // GET request to retrieve user with ID 1
        .then()
                .statusCode(200) // Verify status code is 200 OK
                .body("id", equalTo(1))
                .body("userName", notNullValue())// Verify that the user ID is 1
                .log().all();
    }
    @Test
    public void createNewUser() {

        // Define the payload for creating a new user
        String requestBody = "{ \"userName\": \"John Doe\", \"email\": \"john.doe@example.com\", \"password\": \"password123\" }";

        // Send POST request to create a new user and verify the response
        given()
                .spec(request)
                .header("Content-Type", "application/json") // Set the content type to JSON
                .body(requestBody) // Set the request body
        .when()
                .post("/api/v1/Users") // POST request to create a new user
        .then()
                .statusCode(200)
                .body("userName", equalTo("John Doe"))// Verify status code is 200 Created
                .log().all();
    }
    @Test
    public void updateUserById() {

        // Define the payload for updating the user
        String requestBody = "{ \"userName\": \"John Doe Updated\", \"email\": \"john.doe.updated@example.com\" }";

        // Send PUT request to update the user and verify the response
        given()
                .spec(request)
                .header("Content-Type", "application/json") // Set the content type to JSON
                .body(requestBody) // Set the request body
        .when()
                .put("/api/v1/Users/1") // PUT request to update the user with ID 1
        .then()
                .statusCode(200) // Verify status code is 200 OK
                .body("userName", equalTo("John Doe Updated")); // Verify the name in the response
    }
    @Test
    public void fetchUserByInvalidId() {

        // Send GET request to fetch user by invalid ID and verify the response
        given()
                .spec(request)
                .header("Content-Type", "application/json") // Set the content type to JSON
        .when()
                .get("/api/v1/Users/abc") // GET request with invalid ID 'abc'
        .then()
                .statusCode(400)
                .log().all()
                .body("title",equalTo("One or more validation errors occurred."));
    }
    @Test
    public void createUserWithMissingPassword() {

        // JSON payload with missing email field
        String requestBody = "{ \"userName\": \"Jane Doe\" }";

        // Send POST request to create user with missing email and verify the response
        given()
                .spec(request)
                .header("Content-Type", "application/json") // Set the content type to JSON
                .body(requestBody) // Provide the payload with missing password
        .when()
                .post("/api/v1/Users") // POST request to create user
        .then()
                .statusCode(400)
                .log().all()
                .body("error", equalTo("Bad Request")); // Optionally, verify error message (adjust based on actual response)
    }
    @Test
    public void updateUserWithEmptyPayload() {

        // Send PUT request to update user with empty payload and verify the response
        given()
                .spec(request)
        .when()
                .put("/api/v1/Users/1") // PUT request to update user with ID 1
        .then()
                .statusCode(415)
                .log().all()
                .body("title", equalTo("Unsupported Media Type"));
    }
    @Test
    public void updateUserWithInvalidData() {
        // Invalid JSON payload (userName is of incorrect data type)
        String requestBody = "{ \"userName\": 1234 }";

        // Send PUT request to update user with invalid data and verify the response
        given()
                .spec(request)
                .header("Content-Type", "application/json") // Set the content type to JSON
                .body(requestBody) // Provide the invalid payload
        .when()
                .put("/api/v1/Users/1") // PUT request to update user with ID 1
        .then()
                .statusCode(400)
                .log().all()
                .body("title", equalTo("One or more validation errors occurred.")); // Optionally verify the error message
    }
    @Test
    public void deleteUserWithInvalidId() {

        // Send DELETE request with an invalid user ID and verify the response
        given()
                .spec(request)
                .header("Content-Type", "application/json") // Set the content type to JSON
        .when()
                .delete("/api/v1/Users/9999") // DELETE request for a non-existent user ID
        .then()
                .statusCode(404)
                .log().all()
                .body("error", equalTo("Not Found")); // Optionally verify the error message
    }


}
