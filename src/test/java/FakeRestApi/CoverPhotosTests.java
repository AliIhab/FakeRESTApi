package FakeRestApi;

import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.ResponseAwareMatcher.*;
import static org.hamcrest.Matchers.*;
public class CoverPhotosTests {
    RequestSpecification request;
    @BeforeClass
    public void setup() {
        request = given()
                .log().all()
                .baseUri("https://fakerestapi.azurewebsites.net");
    }
    @Test
    public void fetchAllCoverPhotos() {

        // Send GET request to fetch all cover photos and verify the response
        given()
                .spec(request)
                .contentType("application/json") // Set the content type to JSON
        .when()
                .get("/api/v1/CoverPhotos") // GET request to fetch all cover photos
        .then()
                .statusCode(200) // Verify status code is 200 (OK)
                .body("id", notNullValue()) // Check that the id field is present
                .body("url", notNullValue()); // Check that the url field is present
    }
    @Test
    public void fetchCoverPhotoById() {

        // Send GET request to fetch the cover photo by ID and verify the response
        given()
                .spec(request)
                .contentType("application/json") // Set the content type to JSON
        .when()
                .get("/api/v1/CoverPhotos/1") // GET request to fetch cover photo with ID 1
        .then()
                .statusCode(200) // Verify status code is 200 (OK)
                .body("id", equalTo(1)) // Verify that the id in the response is 1
                .body("url", notNullValue()); // Check that the url field is present
    }
    @Test
    public void createNewCoverPhoto() {
        // Define the payload for creating a new cover photo
        String payload = "{ \"url\": \"https://example.com/image.jpg\", \"description\": \"Cover photo description\" }";

        // Send POST request to create a new cover photo and verify the response
        given()
                .spec(request)
                .contentType("application/json") // Set the content type to JSON
                .body(payload) // Attach the payload
        .when()
                .post("/api/v1/CoverPhotos") // POST request to create a new cover photo
        .then()
                .statusCode(200) // Verify status code is 200 (Created)
                .body("url", equalTo("https://example.com/image.jpg")); // Verify the URL in the response
    }
    @Test
    public void updateCoverPhotoByValidId() {

        // Define the payload for updating the cover photo
        String payload = "{ \"url\": \"https://example.com/new-image.jpg\", \"description\": \"Updated description\" }";

        // Send PUT request to update the cover photo and verify the response
        given()
                .spec(request)
                .contentType("application/json") // Set the content type to JSON
                .body(payload) // Attach the payload
        .when()
                .put("/api/v1/CoverPhotos/1") // PUT request to update cover photo with ID 1
        .then()
                .statusCode(200) // Verify status code is 200 (OK)
                .body("url", equalTo("https://example.com/new-image.jpg")); // Verify the updated URL in the response
    }
    @Test
    public void deleteCoverPhotoByValidId() {

        // Send DELETE request to delete the cover photo and verify the response
        given()
                .spec(request)
                .contentType("application/json") // Set the content type to JSON
        .when()
                .delete("/api/v1/CoverPhotos/1") // DELETE request to remove cover photo with ID 1
        .then()
                .statusCode(200); // Verify status code is 200 (OK)
    }
    @Test
    public void fetchCoverPhotoByInvalidId() {
        // Send GET request to fetch a cover photo with an invalid ID and verify the response
        given()
                .spec(request)
                .contentType("application/json") // Set the content type to JSON
        .when()
                .get("/api/v1/CoverPhotos/abc") // GET request with invalid cover photo ID
        .then()
                .statusCode(400); // Verify status code is 400 (Bad Request)
    }
    @Test
    public void createCoverPhotoWithMissingFields() {
        // Payload with missing 'url' field
        String payload = "{ \"description\": \"Cover photo description\" }";

        // Send POST request to create a cover photo with missing fields and verify the response
        given()
                .spec(request)
                .contentType("application/json") // Set the content type to JSON
                .body(payload) // Set the request payload
        .when()
                .post("/api/v1/CoverPhotos") // POST request to create a cover photo
        .then()
                .statusCode(400)
                .log().all()
                .body("message", equalTo("Field 'url' is required."));
    }
    @Test
    public void createCoverPhotoWithInvalidURL() {
        // Payload with invalid URL
        String payload = "{ \"url\": \"invalid-url\", \"description\": \"Cover photo description\" }";

        // Send POST request to create a cover photo with an invalid URL and verify the response
        given()
                .spec(request)
                .contentType("application/json") // Set the content type to JSON
                .body(payload) // Set the request payload
        .when()
                .post("/api/v1/CoverPhotos") // POST request to create a cover photo
        .then()
                .statusCode(400) // Verify status code is 400 (Bad Request)
                .body("message", equalTo("Invalid URL format.")); // Adjust this based on actual response message
    }
    @Test
    public void updateCoverPhotoWithEmptyPayload() {


        // Send PUT request to update a cover photo with an empty payload and verify the response
        given()
                .spec(request)
        .when()
                .put("/api/v1/CoverPhotos/1") // PUT request to update the cover photo with ID 1
        .then()
                .statusCode(415); // Verify status code is 415 (Unsupported Media Type)
    }
    @Test
    public void updateCoverPhotoWithInvalidData() {
        // Invalid payload
        String payload = "{ \"url\": 1234, \"description\": \"Updated description\" }";

        // Send PUT request to update a cover photo with invalid data and verify the response
        given()
                .spec(request)
                .contentType("application/json") // Set the content type to JSON
                .body(payload) // Set the request payload
        .when()
                .put("/api/v1/CoverPhotos/1") // PUT request to update the cover photo with ID 1
        .then()
                .statusCode(400) // Verify status code is 400 (Bad Request)
                .log().all(); // Adjust this based on actual response message
    }

    @Test
    public void deleteCoverPhotoWithInvalidID() {
        // Invalid Cover Photo ID
        int invalidCoverPhotoId = 9999;

        // Send DELETE request to delete a cover photo with an invalid ID and verify the response
        given()
                .spec(request)
        .when()
                .delete("/api/v1/CoverPhotos/" + invalidCoverPhotoId) // DELETE request to delete the cover photo with invalid ID
        .then()
                .statusCode(404) // Verify status code is 404 (Not Found)
                .body("message", equalTo("Not Found")); // Adjust this based on actual response message
    }


}
