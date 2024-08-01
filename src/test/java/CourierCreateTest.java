import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.Courier;

import java.util.ArrayList;
import java.util.UUID;

import static io.restassured.RestAssured.given;

public class CourierCreateTest {
    private ArrayList<Courier> createdCouriers = new ArrayList<>();

    @Before
    public void setUp() {
        RestAssured.baseURI = Api.baseUrl;
    }

    @Test
    @DisplayName("Create new courier")
    public void testCreateCourier() {
        Courier courier = new Courier(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "Ulyana");
        createdCouriers.add(courier);
        CourierSteps.createCourier(given(), courier);
    }

    @Test
    @DisplayName("Can't create two couriers with same login and password")
    public void cantCreateCouriersWithSameData() {
        Courier courier = new Courier(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "Ulyana");
        createdCouriers.add(courier);
        CourierSteps.createCourier(given(), courier);
        CommonSteps.validateStatus(CourierSteps.createCourierResponse(given(), courier), HttpStatus.SC_CONFLICT);
    }

    @Test
    @DisplayName("Can't create couriers with same login and different passwords")
    public void cantCreateCourierWithSameLoginAndDiffPassword() {
        Courier courier = new Courier(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "Ulyana");
        Courier courier2 = new Courier(courier.getLogin(), UUID.randomUUID().toString(), "Ulyana");
        createdCouriers.add(courier);
        CourierSteps.createCourier(given(), courier);
        CommonSteps.validateStatus(CourierSteps.createCourierResponse(given(), courier2), HttpStatus.SC_CONFLICT);
    }

    @Test
    @DisplayName("Can't create courier without login")
    public void cantCreateCourierWithoutLogin() {
        Courier courier = new Courier("", UUID.randomUUID().toString(), "Ulyana");
        CommonSteps.validateStatus(CourierSteps.createCourierResponse(given(), courier), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Can't create courier without password")
    public void cantCreateCourierWithoutPassword() {
        Courier courier = new Courier(UUID.randomUUID().toString(), "", "Ulyana");
        CommonSteps.validateStatus(CourierSteps.createCourierResponse(given(), courier), HttpStatus.SC_BAD_REQUEST);
    }

    @After
    public void tearDown() {
        for (Courier courier : createdCouriers) {
            deleteCourier(courier);
        }
    }

    private String getCourierId(String login, String password) {
        return given()
                .header("Content-type", "application/json")
                .body(new Courier(login, password, null))
                .post(Api.courierLogin)
                .body()
                .path("id")
                .toString();
    }

    private void deleteCourier(String courierId) {
        given().delete(Api.courier + courierId);
    }

    private void deleteCourier(Courier courier) {
        deleteCourier(getCourierId(courier.getLogin(), courier.getPassword()));
    }
}
