import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.Order;

import static io.restassured.RestAssured.given;

public class OrderCreateSteps {


    @Step("Make create order request")
    public static Response requestOrders(String[] colors) {
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
                .post(Api.ORDERS);
    }
}
