package tests.login;

import annotations.Jira;
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

@Jira(jiraID = "JIRA00001")
@Test(groups = {Groups.REGRESSION, Groups.SANITY, Groups.LOGIN}, testName = "JIRA00001C", description = "JIRA00001D")
public class SuccessfulLoginLogout extends BaseTestClass {

    private final String sTestName = this.getClass().getName();
    private WebDriver driver;

    public final String sJiraID = "JIRA00001A";

    private User user;
    private boolean bCreated = false;


    @BeforeMethod
    public void setUpTest(ITestContext testContext) {
        log.debug("[SETUP TEST] " + sTestName);

        driver = setUpDriver();
        testContext.setAttribute(sTestName + ".drivers", new WebDriver[]{driver});
        testContext.setAttribute(sTestName + ".jiraID", "JIRA00001B");

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
