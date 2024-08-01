import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
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
        RestAssured.baseURI = Api.baseUrl;
        courier = new Courier(UUID.randomUUID().toString(), UUID.randomUUID().toString(), null);
        createCourier(courier);
    }

    @Test
    @DisplayName("Login courier")
    public void login() {
        Response response = login(courier);
        CommonSteps.validateStatus(response, HttpStatus.SC_OK);
        CourierLoginSteps.validateId(response);
    }

    @Test
    @DisplayName("Can't login courier without login")
    public void loginWithoutLogin() {
        Response response = CourierLoginSteps.login(given(), "", courier.getPassword());
        CommonSteps.validateStatus(response, HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Can't login courier without password")
    public void loginWithoutPassword() {
        Response response = CourierLoginSteps.login(given(), courier.getLogin(), "");
        CommonSteps.validateStatus(response, HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Can't login courier with wrong password")
    public void loginErrorWithWrongData() {
        Response response = CourierLoginSteps.login(given(), courier.getLogin(), "password");
        CommonSteps.validateStatus(response, HttpStatus.SC_NOT_FOUND);
    }

    @After
    public void tearDown() {
        deleteCourier(courier);
    }

    private Response login(Courier courier) {
        return CourierLoginSteps.login(given(), courier.getLogin(), courier.getPassword());
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
                .statusCode(HttpStatus.SC_CREATED)
                .body("ok", CoreMatchers.equalTo(true));
    }

    private void deleteCourier(Courier courier) {
        String courierId = given()
                .header("Content-type", "application/json")
                .body(new Courier(courier.getLogin(), courier.getPassword(), null))
                .post(Api.courierLogin)
                .body()
                .path("id")
                .toString();
        given().delete(Api.courier + "/" + courierId);
    }
}
