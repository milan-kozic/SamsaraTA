package tests.login;

import objects.User;
import org.testng.annotations.Test;
import tests.BaseTestClass;
import utils.RestApiUtils;

public class DemoTest extends BaseTestClass {

    @Test
    public void testDemoTest() {

        User user = RestApiUtils.getUser("dedoje");
        String userJson = RestApiUtils.getUserJsonFormat("dedoje");
        log.info(user);
        log.info("\n" + userJson);

    }
}
