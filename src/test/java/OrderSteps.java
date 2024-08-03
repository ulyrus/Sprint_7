import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;

public class OrderSteps {

    @Step("Validate has track field")
    public static void validateTrack(Response response) {
        response.then().body("track", CoreMatchers.notNullValue());
    }

    @Step("Validate has orders field")
    public static void validateHasOrder(Response response) {
        response.then().body("orders", CoreMatchers.hasItems());
    }
}
