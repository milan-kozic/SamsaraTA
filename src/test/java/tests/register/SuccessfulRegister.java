package tests.register;

import data.CommonStrings;
import data.Time;
import objects.User;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.LoginPage;
import pages.RegisterPage;
import tests.BaseTestClass;
import utils.DateTimeUtils;
import utils.RestApiUtils;

import java.util.Date;

public class SuccessfulRegister extends BaseTestClass {

    private final String sTestName = this.getClass().getSimpleName();

    private WebDriver driver;

    private User user;
    private boolean bCreated = false;

    @BeforeMethod
    public void setUpTest(ITestContext testContext) {
        log.debug("[SETUP TEST] " + sTestName);

        driver = setUpDriver();
        testContext.setAttribute(sTestName + ".drivers", new WebDriver[]{driver});

        user = User.createNewUniqueUser("SuccessRegister");
    }

    @Test
    public void testSuccessfulRegister() {

        String sExpectedRegisterSuccessMessage = CommonStrings.getRegisterSuccessMessage();

        log.debug("[START TEST] " + sTestName);

        LoginPage loginPage = new LoginPage(driver).open();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        RegisterPage registerPage = loginPage.clickCreateAccountLink();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        loginPage = registerPage.registerUser(user);
        bCreated = true;
        Date date = DateTimeUtils.getCurrentDateTime();
        user.setCreatedAt(date);
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        String sActualRegisterSuccessMessage = loginPage.getSuccessMessage();
        Assert.assertEquals(sActualRegisterSuccessMessage, sExpectedRegisterSuccessMessage, "Wrong Register Success Message!");

        User savedUser = RestApiUtils.getUser(user.getUsername());
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(savedUser.getEmail(), user.getEmail(), "Email is NOT correct!");
        softAssert.assertEquals(savedUser.getFirstName(), user.getFirstName(), "First Name is NOT correct!");
        softAssert.assertEquals(savedUser.getLastName(), user.getLastName(), "Last Name is NOT correct!");
        softAssert.assertEquals(savedUser.getAbout(), user.getAbout(), "About Text is NOT correct!");
        softAssert.assertTrue(DateTimeUtils.compareDateTimes(savedUser.getCreatedAt(), user.getCreatedAt(), 5));
        softAssert.assertEquals(savedUser.getSecretQuestion(), user.getSecretQuestion(), "Secret Question is NOT correct!");
        softAssert.assertEquals(savedUser.getSecretAnswer(), user.getSecretAnswer(), "Secret Answer is NOT correct!");
        softAssert.assertEquals(savedUser.getHeroCount(), user.getHeroCount(), "Hero Count is NOT correct!");
        softAssert.assertAll("Wrong User Details are saved in Database for User '" + user.getUsername() + "'!");

    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest(ITestResult testResult) {
        log.debug("[END TEST] " + sTestName);
        tearDown(driver, testResult);
        if (bCreated) {
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
