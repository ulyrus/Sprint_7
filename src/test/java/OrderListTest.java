import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class OrderListTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = Api.baseUrl;
    }

    @Test
    @DisplayName("Response contains orders")
    public void testOrder() {
        Response response = given()
                .queryParam("limit", 5)
                .get(Api.orders);
        CommonSteps.validateStatus(response, HttpStatus.SC_OK);
        OrderSteps.validateHasOrder(response);
    }

}
