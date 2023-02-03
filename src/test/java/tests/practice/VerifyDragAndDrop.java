package tests.practice;

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
import pages.PracticePage;
import pages.WelcomePage;
import tests.BaseTestClass;
import utils.DateTimeUtils;
import utils.RestApiUtils;

@Jira(jiraID = "JIRA00003")
@Test(groups = {Groups.REGRESSION, Groups.PRACTICE, Groups.MOUSE})
public class VerifyDragAndDrop extends BaseTestClass {

    private final String sTestName = this.getClass().getName();
    private WebDriver driver;

    private User user;
    private boolean bCreated = false;


    @BeforeMethod
    public void setUpTest(ITestContext testContext) {
        log.debug("[SETUP TEST] " + sTestName);

        driver = setUpDriver();
        testContext.setAttribute(sTestName + ".drivers", new WebDriver[]{driver});

        user = User.createNewUniqueUser("DragAndDrop");
        RestApiUtils.postUser(user);
        bCreated = true;
        user.setCreatedAt(RestApiUtils.getUser(user.getUsername()).getCreatedAt());
    }

    @Test
    public void testVerifyDragAndDrop() {

        String sExpectedDragAndDropMessage = CommonStrings.getDragAndDropMessage();

        log.debug("[START TEST] " + sTestName);

        LoginPage loginPage = new LoginPage(driver).open();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        WelcomePage welcomePage = loginPage.login(user);
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        PracticePage practicePage = welcomePage.clickPracticeTab();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        SoftAssert softAssert1 = new SoftAssert();
        softAssert1.assertTrue(practicePage.isImagePresentInDragArea(), "Draggable Image is NOT present in Drag Area before Drag & Drop action!");
        softAssert1.assertFalse(practicePage.isImagePresentInDropArea(), "Draggable Image should NOT be present in Drop Area before Drag & Drop action!");
        softAssert1.assertFalse(practicePage.isDragAndDropMessageDisplayed(), "Drag and Drop Message should NOT be present before Drag & Drop action!");
        softAssert1.assertAll("Wrong content on Practice Page before Drag & Drop action");

        practicePage = practicePage.dragAndDropImage();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        SoftAssert softAssert2 = new SoftAssert();
        softAssert2.assertFalse(practicePage.isImagePresentInDragArea(), "Draggable Image should NOT be present in Drag Area after Drag & Drop action!");
        softAssert2.assertTrue(practicePage.isImagePresentInDropArea(), "Draggable Image is NOT present in Drop Area after Drag & Drop action!");
        softAssert2.assertTrue(practicePage.isDragAndDropMessageDisplayed(), "Drag and Drop Message is NOT present after Drag & Drop action!");
        softAssert2.assertAll("Wrong content on Practice Page after Drag & Drop action");

        String sDragAndDropMessage = practicePage.getDragAndDropMessage();
        Assert.assertEquals(sDragAndDropMessage, sExpectedDragAndDropMessage, "Wrong Drag and Drop Message");
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
