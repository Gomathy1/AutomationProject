package contract.consumer;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(PactConsumerTestExt.class)
public class UserServiceConsumerContractTest {

    @Pact(consumer = "UserConsumer", provider = "UserProvider")
    public V4Pact getUserByIdPact(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        return builder
            .given("user with id 1 exists")
            .uponReceiving("a request to get user by id")
                .path("/users/1")
                .method("GET")
            .willRespondWith()
                .status(200)
                .headers(headers)
                .body("{\n" +
                    "  \"id\": 1,\n" +
                    "  \"name\": \"John Doe\",\n" +
                    "  \"email\": \"john.doe@example.com\",\n" +
                    "  \"username\": \"johndoe\"\n" +
                    "}")
            .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "getUserByIdPact", port = "8080")
    public void testGetUserById() {
        Response response = given()
            .baseUri("http://localhost:8080")
            .contentType("application/json")
        .when()
            .get("/users/1")
        .then()
            .statusCode(200)
            .extract().response();

        assertThat(response.jsonPath().getInt("id"), equalTo(1));
        assertThat(response.jsonPath().getString("name"), equalTo("John Doe"));
        assertThat(response.jsonPath().getString("email"), equalTo("john.doe@example.com"));
        assertThat(response.jsonPath().getString("username"), equalTo("johndoe"));
    }

    @Pact(consumer = "UserConsumer", provider = "UserProvider")
    public V4Pact getUserNotFoundPact(PactDslWithProvider builder) {
        return builder
            .given("user with id 999 does not exist")
            .uponReceiving("a request to get non-existent user")
                .path("/users/999")
                .method("GET")
            .willRespondWith()
                .status(404)
            .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "getUserNotFoundPact", port = "8080")
    public void testGetNonExistentUser() {
        given()
            .baseUri("http://localhost:8080")
            .contentType("application/json")
        .when()
            .get("/users/999")
        .then()
            .statusCode(404);
    }
}
