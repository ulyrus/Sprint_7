import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import ru.yandex.praktikum.Courier;

import static io.restassured.RestAssured.given;

public class CourierSteps {

    @Step("create courier request")
    public static Response createCourierResponse(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .post(Api.COURIER);
    }

    @Step("create courier")
    public static void createCourier(Courier courier) {
        Response response = createCourierResponse(courier);
        CommonSteps.validateStatus(response, HttpStatus.SC_CREATED);
        validateIsOkTrue(response);
    }

    @Step("get courier")
    public static String getCourierId(String login, String password) {
        return given()
                .header("Content-type", "application/json")
                .body(new Courier(login, password, null))
                .post(Api.COURIER_LOGIN)
                .body()
                .path("id")
                .toString();
    }

    @Step("delete courier by id")
    public static void delete(String id) {
        given().delete(Api.COURIER + id);
    }

    @Step("delete courier using pojo")
    public static void delete(Courier courier) {
        delete(getCourierId(courier.getLogin(), courier.getPassword()));
    }

    @Step("validate statusCode")
    public static void validateIsOkTrue(Response response) {
        response.then().body("ok", CoreMatchers.equalTo(true));
    }
}
