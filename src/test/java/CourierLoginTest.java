import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.Courier;

import java.util.UUID;

public class CourierLoginTest {
    private Courier courier;

    @Before
    public void setUp() {
        RestAssured.baseURI = Api.BASE_URL;
        courier = new Courier(UUID.randomUUID().toString(), UUID.randomUUID().toString(), null);
        CourierSteps.createCourier(courier);
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
        Response response = CourierLoginSteps.login("", courier.getPassword());
        CommonSteps.validateStatus(response, HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Can't login courier without password")
    public void loginWithoutPassword() {
        Response response = CourierLoginSteps.login(courier.getLogin(), "");
        CommonSteps.validateStatus(response, HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Can't login courier with wrong password")
    public void loginErrorWithWrongData() {
        Response response = CourierLoginSteps.login(courier.getLogin(), "password");
        CommonSteps.validateStatus(response, HttpStatus.SC_NOT_FOUND);
    }

    @After
    public void tearDown() {
        CourierSteps.delete(courier);
    }

    private Response login(Courier courier) {
        return CourierLoginSteps.login(courier.getLogin(), courier.getPassword());
    }

}
