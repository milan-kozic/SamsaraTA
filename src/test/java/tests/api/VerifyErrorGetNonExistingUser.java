package tests.api;

import annotations.Jira;
import data.ApiCalls;
import data.CommonStrings;
import data.Groups;
import data.Time;
import objects.ApiError;
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
import tests.BaseTestClass;
import utils.DateTimeUtils;
import utils.PropertiesUtils;
import utils.RestApiUtils;

import java.util.Date;

@Jira(jiraID = "JIRA00010")
@Test(groups = {Groups.API, Groups.USERS})
public class VerifyErrorGetNonExistingUser extends BaseTestClass {

    private final String sTestName = this.getClass().getName();

    private User newUser;

    @BeforeMethod
    public void setUpTest(ITestContext testContext) {
        log.debug("[SETUP TEST] " + sTestName);

        newUser = User.createNewUniqueUser("NonExistingUser");
    }

    @Test
    public void testVerifyErrorGetNonExistingUser() {

        log.debug("[START TEST] " + sTestName);

        int iExpectedStatusCode = 500;
        String sExpectedError = CommonStrings.getApiErrorInternalServerError();
        String sExpectedException = CommonStrings.getApiExceptionIllegalArgumentException();
        String sExpectedMessage = CommonStrings.getApiMessageNonExistingUser(newUser.getUsername());
        String sExpectedPath = ApiCalls.createGetUserApiCall(newUser.getUsername());

        ApiError error = RestApiUtils.getUserApiError(newUser.getUsername());
        Date currentDateTime = DateTimeUtils.getCurrentDateTime();

        log.info("API Error: " + error);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(error.getStatus(), iExpectedStatusCode, "Wrong Status Code!");
        softAssert.assertEquals(error.getError(), sExpectedError, "Wrong Error!");
        softAssert.assertEquals(error.getException(), sExpectedException, "Wrong Exception!");
        softAssert.assertEquals(error.getMessage(), sExpectedMessage, "Wrong Message!");
        softAssert.assertEquals(error.getPath(), sExpectedPath, "Wrong Path!");
        softAssert.assertTrue(DateTimeUtils.compareDateTimes(error.getTimestamp(), currentDateTime, 2), "Wrong Timestamp!");
        softAssert.assertAll("Wrong Error Response Details!");
    }

}
