package tests.profile;

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
import pages.ProfilePage;
import pages.WelcomePage;
import tests.BaseTestClass;
import utils.*;

import java.awt.image.BufferedImage;

@Test(groups = {Groups.REGRESSION, Groups.PROFILE})
public class VerifyProfileImage extends BaseTestClass {

    private final String sTestName = this.getClass().getName();
    private WebDriver driver;

    private User user;

    private boolean bCreated = false;

    @BeforeMethod
    public void setUpTest(ITestContext testContext) {
        LoggerUtils.log.debug("[SETUP TEST] " + sTestName);
        driver = setUpDriver();

        user = User.createNewUniqueUser("ProfileImage");
        RestApiUtils.postUser(user);
        user.setCreatedAt(RestApiUtils.getUser(user.getUsername()).getCreatedAt());
        bCreated = true;
        LoggerUtils.log.info(user);
    }

    @Test
    public void test() {
        LoggerUtils.log.debug("[START TEST] " + sTestName);

        String sProfileImage = "PlagueDoctor.png";
        String sExpectedMessage = CommonStrings.getProfileImageSavedMessage();

        LoginPage loginPage = new LoginPage(driver).open();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        WelcomePage welcomePage = loginPage.login(user);
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        ProfilePage profilePage = welcomePage.clickProfileLink();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        // Upload new image
        profilePage.uploadProfileImage(sProfileImage);
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        // Verify message
        String sActualMessage = profilePage.getProfileImageMessage();
        Assert.assertEquals(sActualMessage, sExpectedMessage, "Wrong Profile Image Message!");
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        BufferedImage actualProfileImage = profilePage.getProfileImageSnapShot();
        DateTimeUtils.wait(Time.TIME_SHORT);
        Assert.assertTrue(ScreenShotUtils.compareSnapShotWithImage(actualProfileImage, sProfileImage, 5, 0, true), "Profile Image is NOT correct!");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest(ITestResult testResult) {
        LoggerUtils.log.debug("[END TEST] " + sTestName);
        tearDown(driver, testResult);
        if(bCreated) {
            cleanUp();
        }
    }

    private void cleanUp() {
        LoggerUtils.log.debug("cleanUp()");
        try {
            RestApiUtils.deleteUser(user.getUsername());
        } catch (AssertionError | Exception e) {
            LoggerUtils.log.error("Cleaning Up Failed! Message: " + e.getMessage());
        }
    }
}
