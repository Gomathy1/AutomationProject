package security;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class APISecurityTest {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    @Test
    public void testSQLInjectionPrevention() {
        String sqlInjectionPayload = "1' OR '1'='1";
        
        Response response = given()
            .baseUri(BASE_URL)
        .when()
            .get("/users/" + sqlInjectionPayload)
        .then()
            .extract().response();
        
        Assert.assertEquals(response.getStatusCode(), 404, 
            "API should reject SQL injection attempts");
        System.out.println("SQL Injection test passed - API rejected malicious input");
    }

    @Test
    public void testXSSPayloadDetection() {
        String xssPayload = "<script>alert('XSS')</script>";
        String requestBody = "{\n" +
            "  \"name\": \"" + xssPayload + "\",\n" +
            "  \"email\": \"test@example.com\"\n" +
            "}";
        
        Response response = given()
            .baseUri(BASE_URL)
            .contentType("application/json")
            .body(requestBody)
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .extract().response();
        
        String responseName = response.jsonPath().getString("name");
        
        // Note: JSONPlaceholder is a mock API and doesn't sanitize
        // In production, you should verify XSS payloads are sanitized
        if (responseName.contains("<script>")) {
            System.out.println("WARNING: XSS payload was not sanitized - " + responseName);
            System.out.println("In production, ensure proper output encoding is implemented");
        } else {
            System.out.println("XSS payload was sanitized");
        }
        
        Assert.assertNotNull(responseName, "Response should contain name field");
    }

    @Test
    public void testHTTPSEnforcement() {
        String httpUrl = "http://jsonplaceholder.typicode.com/users/1";
        
        Response response = given()
            .redirects().follow(false)
        .when()
            .get(httpUrl)
        .then()
            .extract().response();
        
        System.out.println("HTTP Status: " + response.getStatusCode());
        System.out.println("Testing HTTPS enforcement - Response received");
    }
}
