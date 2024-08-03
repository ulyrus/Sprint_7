import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderListSteps {
    public static Response getOrders() {
        return given()
                .queryParam("limit", 5)
                .get(Api.ORDERS);
    }
}
