package tests.login;

import data.CommonStrings;
import data.Groups;
import data.Time;
import objects.User;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.WelcomePage;
import tests.BaseTestClass;
import utils.DateTimeUtils;
import utils.RestApiUtils;

@Test(groups = {Groups.REGRESSION, Groups.SANITY, Groups.LOGIN})
public class SuccessfulLoginLogout extends BaseTestClass {

    private final String sTestName = this.getClass().getName();
    private WebDriver driver;

    private User user;
    private boolean bCreated = false;


    @BeforeMethod
    public void setUpTest(ITestContext testContext) {
        log.debug("[SETUP TEST] " + sTestName);

        driver = setUpDriver();

        user = User.createNewUniqueUser("SuccessLoginLogout");
        RestApiUtils.postUser(user);
        bCreated = true;
        user.setCreatedAt(RestApiUtils.getUser(user.getUsername()).getCreatedAt());
    }

    @Test
    public void testSuccessfulLoginLogout() {

        log.debug("[START TEST] " + sTestName);

        String sExpectedLogoutSuccessMessage = CommonStrings.getLogoutSuccessMessage();

        LoginPage loginPage = new LoginPage(driver).open();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        loginPage.typeUsername(user.getUsername());
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        loginPage.typePassword(user.getPassword());
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        WelcomePage welcomePage = loginPage.clickLoginButton();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        loginPage = welcomePage.clickLogoutLink();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        String sActualLogoutSuccessMessage = loginPage.getSuccessMessage();
        Assert.assertEquals(sActualLogoutSuccessMessage, sExpectedLogoutSuccessMessage, "Wrong Logout Success Message!");

    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest(ITestResult testResult) {
        log.debug("[END TEST] " + sTestName);
        tearDown(driver, testResult);
        if(bCreated) {
            cleanUp();
        }
    }

    private void cleanUp() {
        log.debug("cleanUp()");
        try {
            RestApiUtils.deleteUser(user.getUsername());
        } catch (AssertionError | Exception e) {
            log.error("Exception occurred in cleanUp(" + sTestName + ")! Message: " + e.getMessage());
        }
    }
}
