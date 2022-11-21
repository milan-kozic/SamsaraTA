package tests.page_evaluation;

import data.Groups;
import data.Time;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.LoginPage;
import tests.BaseTestClass;
import utils.DateTimeUtils;

@Test(groups = {Groups.EVALUATION})
public class EvaluateLoginPage extends BaseTestClass {

    private final String sTestName = this.getClass().getName();
    private WebDriver driver;

    @BeforeMethod
    public void setUpTest(ITestContext testContext) {
        log.debug("[SETUP TEST] " + sTestName);

        driver = setUpDriver();
    }

    @Test
    public void testEvaluateLoginPage() {

        log.debug("[START TEST] " + sTestName);

        LoginPage loginPage = new LoginPage(driver).open();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(loginPage.isUsernameTextFieldDisplayed(), "Username Text Field is NOT displayed on Login Page!");
        softAssert.assertTrue(loginPage.isPasswordTextFieldDisplayed(), "Password Text Field is NOT displayed on Login Page!");
        softAssert.assertTrue(loginPage.isLoginButtonDisplayed(), "Login Button is NOT displayed on Login Page!");
        softAssert.assertTrue(loginPage.isCreateAccountLinkDisplayed(), "Create Account Link is NOT displayed on Login Page!");
        softAssert.assertTrue(loginPage.isResetPasswordLinkDisplayed(), "Reset Password Link is NOT displayed on Login Page!");
        softAssert.assertAll("At least one Web Element is NOT displayed on Login Page! Locator(s) changed?");

    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest(ITestResult testResult) {
        log.debug("[END TEST] " + sTestName);
        tearDown(driver, testResult);
    }

}
