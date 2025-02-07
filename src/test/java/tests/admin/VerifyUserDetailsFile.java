package tests.admin;

import annotations.Jira;
import data.Groups;
import data.Time;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.AdminPage;
import pages.LoginPage;
import pages.WelcomePage;
import tests.BaseTestClass;
import utils.*;

@Jira(jiraID = "JIRA0003")
@Test(groups = {Groups.REGRESSION, Groups.ADMIN})
public class VerifyUserDetailsFile extends BaseTestClass {

    private final String sTestName = this.getClass().getName();
    private WebDriver driver;

    private String sAdminUsername;
    private String sAdminPassword;

    private boolean bDownloaded = false;

    private String sFilePath;

    @BeforeMethod
    public void setUpTest(ITestContext testContext) {
        LoggerUtils.log.debug("[SETUP TEST] " + sTestName);

        driver = setUpDriver();
        testContext.setAttribute(sTestName + ".drivers", new WebDriver[]{driver});

        sAdminUsername = PropertiesUtils.getAdminUsername();
        sAdminPassword = PropertiesUtils.getAdminPassword();
    }

    @Test
    public void test() {
        LoggerUtils.log.debug("[START TEST] " + sTestName);

        LoginPage loginPage = new LoginPage(driver).open();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        WelcomePage welcomePage = loginPage.login(sAdminUsername, sAdminPassword);
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        AdminPage adminPage = welcomePage.clickAdminTab();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        sFilePath = adminPage.downloadUserDetailsFile();
        bDownloaded = true;
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        String sFileContent = FileUtils.readTextFile(sFilePath);
        LoggerUtils.log.info("FILE: \n" + sFileContent);

    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest(ITestResult testResult) {
        LoggerUtils.log.debug("[END TEST] " + sTestName);
        tearDown(driver, testResult);
        if(bDownloaded) {
            cleanUp();
        }
    }

    private void cleanUp() {
        LoggerUtils.log.debug("cleanUp()");
        try {
            FileUtils.deleteFile(sFilePath);
        } catch (AssertionError | Exception e) {
            LoggerUtils.log.error("Cleaning Up Failed! Message: " + e.getMessage());
        }
    }
}
