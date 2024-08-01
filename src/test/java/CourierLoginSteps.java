import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.CoreMatchers;
import ru.yandex.praktikum.Courier;

public class CourierLoginSteps {

    @Step("login")
    public static Response login(RequestSpecification given, String login, String password) {
        return given
                .header("Content-type", "application/json")
                .body(new Courier(login, password, null))
                .post(Api.courierLogin);
    }

    @Step("Validate has id field")
    public static void validateId(Response response) {
        response.then().body("id", CoreMatchers.notNullValue());
    }
}
