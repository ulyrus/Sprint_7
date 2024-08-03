import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class OrderListTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = Api.BASE_URL;
    }

    @Test
    @DisplayName("Response contains orders")
    public void testOrder() {
        Response response = OrderListSteps.getOrders();
        CommonSteps.validateStatus(response, HttpStatus.SC_OK);
        OrderSteps.validateHasOrder(response);
    }

}
