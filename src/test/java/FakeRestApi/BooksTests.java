package FakeRestApi;

import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.ResponseAwareMatcher.*;
import static org.hamcrest.Matchers.*;
public class BooksTests {
    RequestSpecification request;
    @BeforeClass
    public void setup() {
        request = given()
                .log().all()
                .baseUri("https://fakerestapi.azurewebsites.net");
    }
    @Test
    public void testFetchAllBooks() {

        // Send GET request to fetch all books and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
        .when()
                .get("/api/v1/Books")  // Endpoint to fetch all books
        .then()
                .assertThat()
                .statusCode(200)  // Assert that the status code is 200 OK
                .body("size()", greaterThan(0))  // Assert that the list of books is not empty
                .body("[0].id", notNullValue())  // Optionally, check that the first book has a valid ID
                .body("[0].title", notNullValue());  // Optionally, check that the first book has a valid title
        // .log().all();  // Optionally log the response for debugging
    }
    @Test
    public void testFetchBookByValidId() {
        // Valid Book ID
        int validBookId = 1;

        // Send GET request to fetch the book by valid ID and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
        .when()
                .get("/api/v1/Books/" + validBookId)  // Endpoint to fetch book by ID
        .then()
                .assertThat()
                .statusCode(200)  // Assert that the status code is 200 OK
                .body("id", equalTo(validBookId))  // Assert that the returned book ID matches the validBookId
                .body("title", notNullValue())  // Check that the book has a valid title
                .body("description", notNullValue())  // Optionally check that the book has a description
                .body("pageCount", greaterThan(0));  // Optionally check that the page count is greater than 0
        // .log().all();  // Optionally log the response for debugging
    }
    @Test
    public void testCreateNewBook() {
        // Valid payload for creating a new book
        String newBookPayload = "{ \"title\": \"New Book Title\", \"author\": \"John Doe\"}";

        // Send POST request to create a new book and validate the response
        given()

                .spec(request)
                .header("Content-Type", "application/json")
                .body(newBookPayload)  // Valid JSON payload
        .when()
                .post("/api/v1/Books")  // Endpoint to create a new book
        .then()
                .assertThat()
                .statusCode(200)  // Assert that the status code is 200 OK
                .body("id", notNullValue())  // Assert that the new book has a valid ID
        .log().all();  // Optionally log the response for debugging
    }
    @Test
    public void testUpdateBookByValidId() {
        // Payload to update book title
        String updateBookPayload = "{\n" +
                "            \"title\": \"Updated Book Title\"\n" +
                "        }";

        // Valid Book ID
        int validBookId = 4;

        // Send PUT request to update the book by valid ID and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
                .body(updateBookPayload)  // Valid JSON payload to update book title
        .when()
                .put("/api/v1/Books/" + validBookId)  // Endpoint to update book by ID
        .then()
                .assertThat()
                .statusCode(200)  // Assert that the status code is 200 OK
        .log().all();  // Optionally log the response for debugging
    }
    @Test
    public void testDeleteBookByValidId() {
        // Valid Book ID to delete
        int validBookId = 15;

        // Send DELETE request to delete the book by valid ID and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
        .when()
                .delete("/api/v1/Books/" + validBookId)  // Endpoint to delete book by ID
        .then()
                .assertThat()
                .statusCode(200) ; // Assert that the status code is 200 OK
        // .log().all();  // Optionally log the response for debugging
    }
    @Test
    public void testFetchBookByInvalidId() {
        // Invalid Book ID to test
        String invalidBookId = "abc";

        // Send GET request to fetch book by invalid ID and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
        .when()
                .get("/api/v1/Books/" + invalidBookId)  // Endpoint to fetch book by invalid ID
        .then()
                .assertThat()
                .statusCode(400);  // Assert that the status code is 400 Bad Request
        // .log().all();  // Optionally log the response for debugging
    }
    @Test
    public void testCreateBookWithMissingFields() {
        // Payload with missing title
        String payload = "{ \"author\": \"John Doe\", \"publishedDate\": \"2024-09-01T00:00:00.000Z\" }";

        // Send POST request to create a book with missing fields and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
                .body(payload)  // Attach the payload
        .when()
                .post("/api/v1/Books")  // Endpoint to create a new book
        .then()
                .assertThat()
                .statusCode(400)  // Assert that the status code is 400 Bad Request
                .body("message", equalTo("Missing required field: title"));  // Optionally check for a specific error message
        // .log().all();  // Optionally log the response for debugging
    }
    @Test
    public void testCreateBookWithInvalidDate() {

        // Payload with invalid date format
        String payload = "{ \"title\": \"New Book\", \"author\": \"John Doe\", \"publishedDate\": \"invalid-date\" }";

        // Send POST request to create a book with invalid date and validate the response
        given()
                .spec(request)
                .header("Content-Type", "application/json")
                .body(payload)  // Attach the payload
        .when()
                .post("/api/v1/Books")  // Endpoint to create a new book
        .then()
                .assertThat()
                .statusCode(400)  // Assert that the status code is 400 Bad Request
                .body("message", equalTo("Invalid date format"));  // Optionally check for a specific error message
        // .log().all();  // Optionally log the response for debugging
    }
    @Test
    public void updateBookWithEmptyPayload() {

        // Send PUT request with empty payload and verify the response body and status code
        given()
                .spec(request)
        .when()
                .put("/api/v1/Books/1")                  // API endpoint to update book with ID 1
        .then()
                .statusCode(415)                  // Verify status code is 415
                .log().all();
    }
    @Test
    public void updateBookWithInvalidData() {
        // Payload with invalid data type for "title" (number instead of string)
        String invalidPayload = "{ \"title\": 1234, \"author\": \"John Doe\", \"publishedDate\": \"2024-09-01T00:00:00.000Z\" }";

        // Send PUT request with invalid payload and verify the response body and status code
        given()
                .spec(request)
                .contentType("application/json")  // Set the content type to JSON
                .body(invalidPayload)             // Payload with invalid data
        .when()
                .put("/api/v1/Books/1")                  // API endpoint to update book with ID 1
        .then()
                .statusCode(400);               // Verify status code is 400 (Bad Request)
    }
    @Test
    public void deleteBookWithInvalidId() {

        // Send DELETE request to an invalid book ID and verify the response body and status code
        given()
                .spec(request)
                .contentType("application/json")  // Set the content type to JSON
        .when()
                .delete("/api/v1/Books/9999")     // DELETE request with invalid book ID
        .then()
                .statusCode(404)                  // Verify status code is 404 (Not Found)
                .body("message", equalTo("Book not found"));  // Verify message in response body
    }
    @Test
    public void createBookWithUnauthorizedUser() {
        // Sample payload for creating a book
        String payload = "{ \"title\": \"New Book Title\", \"author\": \"John Doe\", \"publishedDate\": \"2024-09-01T00:00:00.000Z\" }";

        // Send POST request with unauthorized token and verify the response body and status code
        given()
                .spec(request)
                .contentType("application/json")  // Set the content type to JSON
                .header("Authorization", "Bearer invalid_token") // Set the unauthorized token
                .body(payload)                    // Attach the valid payload
        .when()
                .post("/api/v1/Books")            // POST request to create a book
        .then()
                .statusCode(403)                  // Verify status code is 403 (Forbidden)
                .body("message", equalTo("User not authorized")); // Verify message in response body
    }

}
