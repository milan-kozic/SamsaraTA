package tests.login;

import data.CommonStrings;
import data.Time;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.WelcomePage;
import tests.BaseTestClass;
import utils.DateTimeUtils;
import utils.PropertiesUtils;

public class LoginTestExample extends BaseTestClass {

    @Test
    public void testSuccessfulLoginLogout() {

        WebDriver driver = setUpDriver();
        boolean bSuccess = false;

        try {

            String sUsername = PropertiesUtils.getAdminUsername();
            String sPassword = PropertiesUtils.getAdminPassword();
            String sExpectedLogoutSuccessMessage = CommonStrings.getLogoutSuccessMessage();
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
            bSuccess = true;

        } finally {
            tearDown(driver, bSuccess);
        }
    }

    @Test
    public void testUnsuccessfulLoginWrongPassword() {

        WebDriver driver = setUpDriver();
        boolean bSuccess = false;

        try {

            String sUsername = PropertiesUtils.getAdminUsername();
            String sPassword = PropertiesUtils.getAdminPassword() + "!";
            String sExpectedLoginErrorMessage = CommonStrings.getLoginErrorMessage();

            DateTimeUtils.wait(Time.TIME_SHORT);

            LoginPage loginPage = new LoginPage(driver).open();
            DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

            loginPage.typeUsername(sUsername);
            DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

            loginPage.typePassword(sPassword);
            DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

            loginPage = loginPage.clickLoginButtonNoProgress();
            DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

            String sActualLoginErrorMessage = loginPage.getErrorMessage();
            Assert.assertEquals(sActualLoginErrorMessage, sExpectedLoginErrorMessage, "Wrong Login Error Message!");
            bSuccess = true;

        } finally {
            tearDown(driver, bSuccess);
        }
    }
}
