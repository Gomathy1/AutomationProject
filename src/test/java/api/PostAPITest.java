package api;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PostAPITest extends BaseAPITest {

    @Test
    public void testGetPostById() {
        int postId = 1;
        
        given()
            .spec(requestSpec)
        .when()
            .get("/posts/" + postId)
        .then()
            .statusCode(200)
            .body("id", equalTo(postId))
            .body("userId", notNullValue())
            .body("title", notNullValue())
            .body("body", notNullValue());
    }

    @Test
    public void testGetPostsByUserId() {
        int userId = 1;
        
        Response response = given()
            .spec(requestSpec)
            .queryParam("userId", userId)
        .when()
            .get("/posts")
        .then()
            .statusCode(200)
            .body("userId", everyItem(equalTo(userId)))
            .extract().response();
        
        int postCount = response.jsonPath().getList("$").size();
        Assert.assertTrue(postCount > 0, "User should have at least one post");
        System.out.println("User " + userId + " has " + postCount + " posts");
    }

    @Test
    public void testCreatePost() {
        String requestBody = "{\n" +
            "  \"userId\": 1,\n" +
            "  \"title\": \"Test Post Title\",\n" +
            "  \"body\": \"This is a test post body content\"\n" +
            "}";
        
        Response response = given()
            .spec(requestSpec)
            .body(requestBody)
        .when()
            .post("/posts")
        .then()
            .statusCode(201)
            .body("userId", equalTo(1))
            .body("title", equalTo("Test Post Title"))
            .body("body", equalTo("This is a test post body content"))
            .body("id", notNullValue())
            .extract().response();
        
        int createdPostId = response.jsonPath().getInt("id");
        System.out.println("Created Post ID: " + createdPostId);
    }
}
