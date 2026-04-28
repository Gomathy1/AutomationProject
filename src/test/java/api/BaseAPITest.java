package api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public class BaseAPITest {
    
    protected RequestSpecification requestSpec;
    
    @BeforeClass
    public void setupAPI() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
        
        requestSpec = new RequestSpecBuilder()
            .setContentType("application/json")
            .setAccept("application/json")
            .build();
    }
}
