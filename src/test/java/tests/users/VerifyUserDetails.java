package tests.users;

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
import org.testng.asserts.SoftAssert;
import pages.LoginPage;
import pages.UserDetailsDialogBox;
import pages.UsersPage;
import pages.WelcomePage;
import tests.BaseTestClass;
import utils.DateTimeUtils;
import utils.RestApiUtils;

@Jira(jiraID = "JIRA00003")
@Test(groups = {Groups.REGRESSION, Groups.SANITY, Groups.DEMO, Groups.USERS})
public class VerifyUserDetails extends BaseTestClass {

    private final String sTestName = this.getClass().getName();
    private WebDriver driver;
    private User user;
    private boolean bCreated = false;


    @BeforeMethod
    public void setUpTest(ITestContext testContext) {
        log.debug("[SETUP TEST] " + sTestName);

        driver = setUpDriver();
        testContext.setAttribute(sTestName + ".drivers", new WebDriver[]{driver});

        user = User.createNewUniqueUser("VerifyUserDetails");
        RestApiUtils.postUser(user);
        bCreated = true;
        user.setCreatedAt(RestApiUtils.getUser(user.getUsername()).getCreatedAt());
    }

    @Test
    public void testVerifyUserDetails() {

        log.debug("[START TEST] " + sTestName);

        LoginPage loginPage = new LoginPage(driver).open();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        WelcomePage welcomePage = loginPage.login(user);
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        UsersPage usersPage = welcomePage.clickUsersTab();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        usersPage = usersPage.search(user.getUsername());
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        UserDetailsDialogBox userDetailsDialogBox = usersPage.clickUserDetailsIconInUsersTable(user.getUsername());
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(userDetailsDialogBox.getUsername(), user.getUsername(), "Username is NOT correct!");
        softAssert.assertEquals(userDetailsDialogBox.getFirstName(), user.getFirstName(), "First Name is NOT correct!");
        softAssert.assertEquals(userDetailsDialogBox.getLastName(), user.getLastName(), "Last Name is NOT correct!");
        softAssert.assertEquals(userDetailsDialogBox.getAbout(), user.getAbout(), "About Text is NOT correct!");
        softAssert.assertTrue(DateTimeUtils.compareDateTimes(userDetailsDialogBox.getCreatedAtDate(), user.getCreatedAt(), 120), "Created At Date is NOT correct!");
        softAssert.assertAll("User Details are NOT correct!");
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
