package tests.profile;

import annotations.Jira;
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
import pages.ProfilePage;
import pages.WelcomePage;
import tests.BaseTestClass;
import utils.DateTimeUtils;
import utils.RestApiUtils;
import utils.ScreenShotUtils;

@Jira(jiraID = "JIRA00006")
@Test(groups = {Groups.REGRESSION, Groups.PRACTICE, Groups.IMAGE})
public class VerifyProfileImage extends BaseTestClass {

    private final String sTestName = this.getClass().getName();
    private WebDriver driver;

    private User user;
    private boolean bCreated = false;


    @BeforeMethod
    public void setUpTest(ITestContext testContext) {
        log.debug("[SETUP TEST] " + sTestName);

        driver = setUpDriver();
        testContext.setAttribute(sTestName + ".drivers", new WebDriver[]{driver});

        user = User.createNewUniqueUser("ProfileImage");
        RestApiUtils.postUser(user);
        bCreated = true;
        user.setCreatedAt(RestApiUtils.getUser(user.getUsername()).getCreatedAt());
    }

    @Test
    public void testVerifyProfileImage() {

        log.debug("[START TEST] " + sTestName);

        String sProfileImageFile = "ProfileImage.png";

        LoginPage loginPage = new LoginPage(driver).open();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        WelcomePage welcomePage = loginPage.login(user);
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        ProfilePage profilePage = welcomePage.clickProfileLink();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        //profilePage.saveProfileImageSnapShot();
        //Assert.assertTrue(ScreenShotUtils.compareSnapShotWithImageAS(profilePage.getProfileImageSnapShotAS(), sProfileImageFile, 20, 5));
        Assert.assertTrue(ScreenShotUtils.compareSnapShotWithImage(profilePage.getProfileImageSnapShot(), sProfileImageFile, 10, 5));
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
