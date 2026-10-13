package io.quarkus.workshop.superheroes.villain;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class VillainResourceTest {
    @Test
    void testHelloEndpoint() {
        given()
          .when().get("/api/villains/hello")
          .then()
             .statusCode(200)
             .body(is("Hello Villain Resource"));
    }
    @Test
    void shouldPingOpenAPI() {
        given()
            .header("Accept", "application/json")
            .when().get("/q/openapi")
            .then()
            .statusCode(200);
    }
}
