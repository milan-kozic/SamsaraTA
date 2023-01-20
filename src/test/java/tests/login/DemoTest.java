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

        Date date = DateTimeUtils.getCurrentDateTime();
        log.info(date);
        String sLocaleDateTime = DateTimeUtils.getLocalizedDateTime(date, "EEEE, dd-MMMM-yyyy HH:mm:ss zzzz", "sl");
        log.info(sLocaleDateTime);
    }
}
