import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.Courier;

import java.util.ArrayList;
import java.util.UUID;

public class CourierCreateTest {
    private ArrayList<Courier> createdCouriers = new ArrayList<>();

    @Before
    public void setUp() {
        RestAssured.baseURI = Api.BASE_URL;
    }

    @Test
    @DisplayName("Create new courier")
    public void testCreateCourier() {
        Courier courier = new Courier(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "Ulyana");
        createdCouriers.add(courier);
        CourierSteps.createCourier(courier);
    }

    @Test
    @DisplayName("Can't create two couriers with same login and password")
    public void cantCreateCouriersWithSameData() {
        Courier courier = new Courier(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "Ulyana");
        createdCouriers.add(courier);
        CourierSteps.createCourier(courier);
        CommonSteps.validateStatus(CourierSteps.createCourierResponse(courier), HttpStatus.SC_CONFLICT);
    }

    @Test
    @DisplayName("Can't create couriers with same login and different passwords")
    public void cantCreateCourierWithSameLoginAndDiffPassword() {
        Courier courier = new Courier(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "Ulyana");
        Courier courier2 = new Courier(courier.getLogin(), UUID.randomUUID().toString(), "Ulyana");
        createdCouriers.add(courier);
        CourierSteps.createCourier(courier);
        CommonSteps.validateStatus(CourierSteps.createCourierResponse(courier2), HttpStatus.SC_CONFLICT);
    }

    @Test
    @DisplayName("Can't create courier without login")
    public void cantCreateCourierWithoutLogin() {
        Courier courier = new Courier("", UUID.randomUUID().toString(), "Ulyana");
        CommonSteps.validateStatus(CourierSteps.createCourierResponse(courier), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Can't create courier without password")
    public void cantCreateCourierWithoutPassword() {
        Courier courier = new Courier(UUID.randomUUID().toString(), "", "Ulyana");
        CommonSteps.validateStatus(CourierSteps.createCourierResponse(courier), HttpStatus.SC_BAD_REQUEST);
    }

    @After
    public void tearDown() {
        for (Courier courier : createdCouriers) {
            CourierSteps.delete(courier);
        }
    }
}
