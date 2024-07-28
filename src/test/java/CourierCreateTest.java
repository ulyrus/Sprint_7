import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.Courier;

import java.util.UUID;

import static io.restassured.RestAssured.given;

public class CourierCreateTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Create new courier")
    public void testCreateCourier() {
        Courier courier = new Courier(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "Ulyana");
        createCourier(courier);
        deleteCourier(courier);
    }

    @Test
    @DisplayName("Can't create two couriers with same login and password")
    public void cantCreateCouriersWithSameData() {
        Courier courier = new Courier(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "Ulyana");
        createCourier(courier);
        validateStatusCode(createCourierResponse(courier), 409);
        deleteCourier(courier);
    }

    @Test
    @DisplayName("Can't create couriers with same login and different passwords")
    public void cantCreateCourierWithSameLoginAndDiffPassword() {
        Courier courier = new Courier(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "Ulyana");
        Courier courier2 = new Courier(courier.getLogin(), UUID.randomUUID().toString(), "Ulyana");
        createCourier(courier);
        validateStatusCode(createCourierResponse(courier2), 409);
        deleteCourier(courier);
    }

    @Test
    @DisplayName("Can't create courier without login")
    public void cantCreateCourierWithoutLogin() {
        Courier courier = new Courier("", UUID.randomUUID().toString(), "Ulyana");
        validateStatusCode(createCourierResponse(courier), 400);
    }

    @Test
    @DisplayName("Can't create courier without password")
    public void cantCreateCourierWithoutPassword() {
        Courier courier = new Courier(UUID.randomUUID().toString(), "", "Ulyana");
        validateStatusCode(createCourierResponse(courier), 400);
    }

    @Step("create courier request")
    private Response createCourierResponse(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier");
    }

    @Step("create courier")
    private void createCourier(Courier courier) {
        Response response = createCourierResponse(courier);
        validateStatusCode(response, 201);
        validateIsOkTrue(response);
    }

    @Step("validate statusCode")
    private void validateStatusCode(Response response, int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Step("validate statusCode")
    private void validateIsOkTrue(Response response) {
        response.then().body("ok", CoreMatchers.equalTo(true));
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
