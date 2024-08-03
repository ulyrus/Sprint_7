import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

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
        RestAssured.baseURI = Api.BASE_URL;
    }

    @Test
    @DisplayName("Create order")
    public void createOrder() {
        Response response = OrderCreateSteps.requestOrders(colors);
        CommonSteps.validateStatus(response, HttpStatus.SC_CREATED);
        OrderSteps.validateTrack(response);
    }

    private static final String COLOR_BLACK = "BLACK";
    private static final String COLOR_GREY = "GREY";
}
