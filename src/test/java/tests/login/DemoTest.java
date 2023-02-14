package tests.login;

import objects.User;
import org.testng.annotations.Test;
import tests.BaseTestClass;
import utils.DateTimeUtils;
import utils.RestApiUtils;

import java.util.Date;

public class DemoTest extends BaseTestClass {

    @Test
    public void testDemoTest() {

        User newUser = User.readUserFromCSVFile("dedoje");
        log.info(newUser);
    }
}
