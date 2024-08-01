import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import ru.yandex.praktikum.Courier;

public class CourierSteps {

    @Step("create courier request")
    public static Response createCourierResponse(RequestSpecification given, Courier courier) {
        return given
                .header("Content-type", "application/json")
                .body(courier)
                .post(Api.courier);
    }

    @Step("create courier")
    public static void createCourier(RequestSpecification given, Courier courier) {
        Response response = createCourierResponse(given, courier);
        CommonSteps.validateStatus(response, HttpStatus.SC_CREATED);
        validateIsOkTrue(response);
    }

    @Step("validate statusCode")
    public static void validateIsOkTrue(Response response) {
        response.then().body("ok", CoreMatchers.equalTo(true));
    }
}
