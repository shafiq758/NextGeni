package utils;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {

    @BeforeAll
    public static void setup() {
        // Setup base URI for the tests
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
    }
}
