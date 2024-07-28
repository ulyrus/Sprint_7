import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.FailureConfig;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.Courier;

import java.util.UUID;

import static io.restassured.RestAssured.given;

public class CourierLoginTest {
    private Courier courier;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        courier = new Courier(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        createCourier(courier);
    }

    @Test
    @DisplayName("Login courier")
    public void login() {
        Response response = login(courier);
        validateStatus(response, 200);
        validateId(response);
    }

    @Test
    @DisplayName("Can't login courier without login")
    public void loginWithoutLogin() {
        Response response = login("", courier.getPassword());
        validateStatus(response, 400);
    }

    @Test
    @DisplayName("Can't login courier without password")
    public void loginWithoutPassword() {
        Response response = login(courier.getLogin(), "");
        validateStatus(response, 400);
    }

    @Test
    @DisplayName("Can't login courier with wrong password")
    public void loginErrorWithWrongData() {
        Response response = login(courier.getLogin(), "password");
        validateStatus(response, 404);
    }

    @After
    public void tearDown() {
        deleteCourier(courier);
    }

    private Response login(Courier courier) {
        return login(courier.getLogin(), courier.getPassword());
    }

    @Step("login")
    private Response login(String login, String password) {
        return given()
                .header("Content-type", "application/json")
                .body(new Courier(login, password))
                .post("/api/v1/courier/login");
    }

    @Step("Validate statusCode")
    private void validateStatus(Response response, int statusCode) {
        response
                .then()
                .statusCode(statusCode);
    }

    @Step("Validate has id field")
    private void validateId(Response response) {
        response.then().body("id", CoreMatchers.notNullValue());
    }

    private Response createCourierResponse(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier");
    }

    private void createCourier(Courier courier) {
        createCourierResponse(courier)
                .then()
                .statusCode(201)
                .body("ok", CoreMatchers.equalTo(true));
    }

    private String getCourierId(String login, String password) {
        return given()
                .header("Content-type", "application/json")
                .body(new Courier(login, password))
                .post("/api/v1/courier/login")
                .body()
                .path("id")
                .toString();
    }

    private void deleteCourier(String courierId) {
        given().delete("/api/v1/courier/" + courierId);
    }

    private void deleteCourier(Courier courier) {
        deleteCourier(getCourierId(courier.getLogin(), courier.getPassword()));
    }
}
