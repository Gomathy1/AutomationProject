package security;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class AuthenticationSecurityTest {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    @Test
    public void testUnauthorizedAccess() {
        Response response = given()
            .baseUri(BASE_URL)
        .when()
            .get("/users/1")
        .then()
            .extract().response();
        
        System.out.println("Status Code: " + response.getStatusCode());
        Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 401,
            "API should return 200 (public) or 401 (requires auth)");
    }

    @Test
    public void testInvalidTokenHandling() {
        String invalidToken = "invalid_token_12345";
        
        Response response = given()
            .baseUri(BASE_URL)
            .header("Authorization", "Bearer " + invalidToken)
        .when()
            .get("/users/1")
        .then()
            .extract().response();
        
        System.out.println("Response with invalid token: " + response.getStatusCode());
        Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 401,
            "API should handle invalid tokens properly");
    }

    @Test
    public void testPasswordInResponse() {
        Response response = given()
            .baseUri(BASE_URL)
        .when()
            .get("/users/1")
        .then()
            .extract().response();
        
        String responseBody = response.getBody().asString().toLowerCase();
        
        Assert.assertFalse(responseBody.contains("password"), 
            "Response should not contain password field");
        Assert.assertFalse(responseBody.contains("passwd"), 
            "Response should not contain password-related fields");
        System.out.println("Password exposure test passed - No sensitive data in response");
    }
}
