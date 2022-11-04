package tests.login;

import data.CommonStrings;
import data.Time;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.WelcomePage;
import tests.BaseTestClass;
import utils.DateTimeUtils;
import utils.PropertiesUtils;

public class LoginTestExample extends BaseTestClass {

    @Test
    public void testExample1() {

        WebDriver driver = setUpDriver();

        try {

            String sUsername = PropertiesUtils.getAdminUsername();
            String sPassword = PropertiesUtils.getAdminPassword();
            String sExpectedLogoutSuccessMessage = CommonStrings.LOGOUT_SUCCESS_MESSAGE;
            DateTimeUtils.wait(Time.TIME_SHORT);

            LoginPage loginPage = new LoginPage(driver).open();
            DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

            loginPage.typeUsername(sUsername);
            DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

            loginPage.typePassword(sPassword);
            DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

            WelcomePage welcomePage = loginPage.clickLoginButton();
            DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

            loginPage = welcomePage.clickLogoutLink();
            DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

            String sActualLogoutSuccessMessage = loginPage.getSuccessMessage();
            Assert.assertEquals(sActualLogoutSuccessMessage, sExpectedLogoutSuccessMessage, "Wrong Logout Success Message!");

        } finally {
            quitDriver(driver);
        }
    }
}
