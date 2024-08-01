import io.qameta.allure.Step;
import io.restassured.response.Response;

public class CommonSteps {
    @Step("Validate statusCode")
    public static void validateStatus(Response response, int statusCode) {
        response
                .then()
                .statusCode(statusCode);
    }
}
