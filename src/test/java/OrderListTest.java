import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.Courier;

import java.util.UUID;

import static io.restassured.RestAssured.given;

public class OrderListTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Response contains orders")
    public void testOrder() {
        Response response = given()
                .queryParam("limit", 5)
                .get("/api/v1/orders");
        validateStatus(response, 200);
        validateHasOrder(response);
    }

    @Step("Validate statusCode")
    private void validateStatus(Response response, int statusCode) {
        response
                .then()
                .statusCode(statusCode);
    }

    @Step("Validate has orders field")
    private void validateHasOrder(Response response) {
        response.then().body("orders", CoreMatchers.hasItems());
    }

}
