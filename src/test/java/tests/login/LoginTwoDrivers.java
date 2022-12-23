package tests.login;

import data.Groups;
import data.Time;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HeroesPage;
import pages.LoginPage;
import pages.UsersPage;
import pages.WelcomePage;
import tests.BaseTestClass;
import utils.DateTimeUtils;
import utils.PropertiesUtils;

@Test(groups = {Groups.REGRESSION, Groups.LOGIN})
public class LoginTwoDrivers extends BaseTestClass {

    private final String sTestName = this.getClass().getName();
    private WebDriver driver1;
    private WebDriver driver2;

    String sAdminUsername;
    String sAdminPassword;

    String sEndUserUsername;
    String sEndUserPassword;

    @BeforeMethod
    public void setUpTest(ITestContext testContext) {
        log.debug("[SETUP TEST] " + sTestName);

        driver1 = setUpDriver();
        driver2 = setUpDriver();
        testContext.setAttribute(sTestName + ".drivers", new WebDriver[]{driver1, driver2});

        sAdminUsername = PropertiesUtils.getAdminUsername();
        sAdminPassword = PropertiesUtils.getAdminPassword();
        sEndUserUsername = PropertiesUtils.getEndUserUsername();
        sEndUserPassword = PropertiesUtils.getEndUserPassword();
    }

    @Test
    public void testLoginTwoDrivers() {

        log.debug("[START TEST] " + sTestName);

        LoginPage loginPage1 = new LoginPage(driver1).open();
        log.info("Login Page Title: " + loginPage1.getPageTitle());
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        LoginPage loginPage2 = new LoginPage(driver2).open();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        WelcomePage welcomePage1 = loginPage1.login(sAdminUsername, sAdminPassword);
        log.info("Welcome Page Title: " + welcomePage1.getPageTitle());
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        WelcomePage welcomePage2 = loginPage2.login(sEndUserUsername, sEndUserPassword);
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        UsersPage usersPage1 = welcomePage1.clickUsersTab();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        HeroesPage heroesPage2 = welcomePage2.clickHeroesTab();
        DateTimeUtils.wait(Time.TIME_SHORT);

    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest(ITestResult testResult) {
        log.debug("[END TEST] " + sTestName);
        tearDown(driver1, testResult, 1);
        tearDown(driver2, testResult, 2);
    }
}
