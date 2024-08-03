import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import ru.yandex.praktikum.Courier;

import static io.restassured.RestAssured.given;

public class CourierLoginSteps {

    @Step("login")
    public static Response login(String login, String password) {
        return given()
                .header("Content-type", "application/json")
                .body(new Courier(login, password, null))
                .post(Api.COURIER_LOGIN);
    }

    @Step("Validate has id field")
    public static void validateId(Response response) {
        response.then().body("id", CoreMatchers.notNullValue());
    }
}
