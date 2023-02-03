package tests.api;

import annotations.Jira;
import data.ApiCalls;
import data.CommonStrings;
import data.Groups;
import data.Time;
import objects.ApiError;
import objects.User;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import tests.BaseTestClass;
import utils.DateTimeUtils;
import utils.RestApiUtils;

import java.util.Date;

@Jira(jiraID = "JIRA00012")
@Test(groups = {Groups.API, Groups.USERS})
public class VerifyErrorGetUserNoPermission extends BaseTestClass {

    private final String sTestName = this.getClass().getName();

    private User newUser;

    boolean bCreated = false;

    @BeforeMethod
    public void setUpTest(ITestContext testContext) {
        log.debug("[SETUP TEST] " + sTestName);

        newUser = User.createNewUniqueUser("GetUserNoPermission");
        RestApiUtils.postUser(newUser);
        bCreated = true;
        newUser.setCreatedAt(RestApiUtils.getUser(newUser.getUsername()).getCreatedAt());

    }

    @Test
    public void testVerifyErrorGetUserNoPermission() {

        log.debug("[START TEST] " + sTestName);

        int iExpectedStatusCode = 403;
        String sExpectedError = CommonStrings.getApiErrorForbidden();
        String sExpectedMessage = CommonStrings.getApiMessageAccessDenied();
        String sExpectedPath = ApiCalls.createGetUserApiCall(newUser.getUsername());
        ApiError error = RestApiUtils.getUserApiError(newUser.getUsername(), newUser.getUsername(), newUser.getPassword());
        Date currentDateTime = DateTimeUtils.getCurrentDateTime();

        log.info("API Error: " + error);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(error.getStatus(), iExpectedStatusCode, "Wrong Status Code!");
        softAssert.assertEquals(error.getError(), sExpectedError, "Wrong Error!");
        softAssert.assertNull(error.getException(), "Exception should NOT exist!");
        softAssert.assertEquals(error.getMessage(), sExpectedMessage, "Wrong Message!");
        softAssert.assertEquals(error.getPath(), sExpectedPath, "Wrong Path!");
        softAssert.assertTrue(DateTimeUtils.compareDateTimes(error.getTimestamp(), currentDateTime, 2), "Wrong Timestamp!");
        softAssert.assertAll("Wrong Error Response Details!");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest(ITestResult testResult) {
        log.debug("[END TEST] " + sTestName);
        if(bCreated) {
            cleanUp();
        }
    }

    private void cleanUp() {
        log.debug("cleanUp()");
        try {
            RestApiUtils.deleteUser(newUser.getUsername());
        } catch (AssertionError | Exception e) {
            log.error("Exception occurred in cleanUp(" + sTestName + ")! Message: " + e.getMessage());
        }
    }
}
