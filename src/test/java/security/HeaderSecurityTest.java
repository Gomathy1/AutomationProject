package security;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class HeaderSecurityTest {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    @Test
    public void testSecurityHeaders() {
        Response response = given()
            .baseUri(BASE_URL)
        .when()
            .get("/users/1")
        .then()
            .extract().response();
        
        System.out.println("=== Security Headers Check ===");
        
        String xContentType = response.getHeader("X-Content-Type-Options");
        System.out.println("X-Content-Type-Options: " + xContentType);
        
        String xFrameOptions = response.getHeader("X-Frame-Options");
        System.out.println("X-Frame-Options: " + xFrameOptions);
        
        String strictTransport = response.getHeader("Strict-Transport-Security");
        System.out.println("Strict-Transport-Security: " + strictTransport);
        
        String contentSecurityPolicy = response.getHeader("Content-Security-Policy");
        System.out.println("Content-Security-Policy: " + contentSecurityPolicy);
    }

    @Test
    public void testCORSHeaders() {
        Response response = given()
            .baseUri(BASE_URL)
            .header("Origin", "https://example.com")
        .when()
            .get("/users/1")
        .then()
            .extract().response();
        
        String corsHeader = response.getHeader("Access-Control-Allow-Origin");
        System.out.println("CORS Header (Access-Control-Allow-Origin): " + corsHeader);
        
        if (corsHeader != null) {
            Assert.assertNotEquals(corsHeader, "*", 
                "CORS should not allow all origins in production");
        }
    }

    @Test
    public void testServerHeaderExposure() {
        Response response = given()
            .baseUri(BASE_URL)
        .when()
            .get("/users/1")
        .then()
            .extract().response();
        
        String serverHeader = response.getHeader("Server");
        System.out.println("Server Header: " + serverHeader);
        
        if (serverHeader != null) {
            System.out.println("Warning: Server header exposes server information");
        } else {
            System.out.println("Good: Server header is hidden");
        }
    }
}
