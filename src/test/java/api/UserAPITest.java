package api;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserAPITest extends BaseAPITest {

    @Test
    public void testGetUserById() {
        int userId = 1;
        
        Response response = given()
            .spec(requestSpec)
        .when()
            .get("/users/" + userId)
        .then()
            .statusCode(200)
            .body("id", equalTo(userId))
            .body("name", notNullValue())
            .body("email", containsString("@"))
            .extract().response();
        
        System.out.println("User Name: " + response.jsonPath().getString("name"));
        System.out.println("User Email: " + response.jsonPath().getString("email"));
    }

    @Test
    public void testCreateUser() {
        String requestBody = "{\n" +
            "  \"name\": \"Gomathy Test\",\n" +
            "  \"username\": \"gomathy.test\",\n" +
            "  \"email\": \"gomathy@test.com\"\n" +
            "}";
        
        Response response = given()
            .spec(requestSpec)
            .body(requestBody)
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .body("name", equalTo("Gomathy Test"))
            .body("username", equalTo("gomathy.test"))
            .body("email", equalTo("gomathy@test.com"))
            .body("id", notNullValue())
            .extract().response();
        
        int createdUserId = response.jsonPath().getInt("id");
        Assert.assertTrue(createdUserId > 0, "Created user should have valid ID");
        System.out.println("Created User ID: " + createdUserId);
    }

    @Test
    public void testDeleteUser() {
        int userId = 1;
        
        given()
            .spec(requestSpec)
        .when()
            .delete("/users/" + userId)
        .then()
            .statusCode(200);
        
        System.out.println("User " + userId + " deleted successfully");
    }
}
