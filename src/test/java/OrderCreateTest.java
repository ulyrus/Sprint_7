import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.Order;

import java.util.List;

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
        RestAssured.baseURI = Api.baseUrl;
    }

    @Test
    @DisplayName("Create order")
    public void createOrder() {
        Response response = requestOrders(colors);
        CommonSteps.validateStatus(response, HttpStatus.SC_CREATED);
        OrderSteps.validateTrack(response);
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
                .post(Api.orders);
    }

    private static final String COLOR_BLACK = "BLACK";
    private static final String COLOR_GREY = "GREY";
}
