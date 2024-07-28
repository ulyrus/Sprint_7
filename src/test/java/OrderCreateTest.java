import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.Courier;
import ru.yandex.praktikum.Order;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    @Parameterized.Parameters
    public static List<List<String>> getData() {
        return List.of(
               List.of(),
               List.of(COLOR_BLACK),
               List.of(COLOR_GREY),
               List.of(COLOR_BLACK, COLOR_GREY)
        );
    }

    private final String[] colors;

    public OrderCreateTest(List<String> colors) {
        this.colors = colors.toArray(new String[] {});
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Create order")
    public void createOrder() {
        Response response = requestOrders(colors);
        validateStatus(response, 201);
        validateTrack(response);
    }

    @Step("Make create order request")
    private Response requestOrders(String[] colors) {
        Order order = new Order(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                "4",
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                colors
        );
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .post("/api/v1/orders");
    }

    @Step("Validate statusCode")
    private void validateStatus(Response response, int statusCode) {
        response
                .then()
                .statusCode(statusCode);
    }

    @Step("Validate has track field")
    private void validateTrack(Response response) {
        response.then().body("track", CoreMatchers.notNullValue());
    }

    private static final String COLOR_BLACK = "BLACK";
    private static final String COLOR_GREY = "GREY";
}
