package listeners;

import org.openqa.selenium.WebDriver;
import org.testng.*;
import utils.LoggerUtils;

public class TestListener implements ITestListener, ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        String sSuiteName = suite.getName();
        LoggerUtils.log.info(("[SUITE STARTED] " + sSuiteName));
    }

    @Override
    public void onFinish(ISuite suite) {
        String sSuiteName = suite.getName();
        LoggerUtils.log.info(("[SUITE FINISHED] " + sSuiteName));
    }

    @Override
    public void onStart(ITestContext context) {
        String sTestGroupName = context.getName();
        LoggerUtils.log.info(("[TESTS STARTED] " + sTestGroupName));

        // Get parameters from suite/properties/command line... etc...

    }

    @Override
    public void onFinish(ITestContext context) {
        String sTestGroupName = context.getName();
        LoggerUtils.log.info(("[TESTS FINISHED] " + sTestGroupName));

    }

    @Override
    public void onTestStart(ITestResult result) {
        // Test name at the Class level (if exists) or test method name
        //String sTestName1 = result.getName();
        // Test name at the Class level (if exists) or NULL (or Test name at the Method level if @Test annotation doesn't exist at the class level)
        //String sTestName2 = result.getTestName();
        // Test Class Name (full path) - for One Test Case -> One Class -> One Method approach
        //String sTestName3 = result.getTestClass().getName();
        // Test name at the Class level (if exists) or Test name at the Method level (if exists) or NULL
        //String sTestName4 = result.getTestClass().getTestName();
        // Test method name (without path)
        //String sTestName5 = result.getMethod().getMethodName();
        // Test method name (full path) - for unit and integration test (multiple test methods inside one test class)
        //String sTestName6 = result.getMethod().getQualifiedName();
        // Test Class Name (without path)
        //String sTestName7 = result.getTestClass().getRealClass().getSimpleName();

        String sTestName = result.getTestClass().getName();
        LoggerUtils.log.info(("[TEST STARTED] " + sTestName));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        LoggerUtils.log.info(("[TEST SUCCESS] " + sTestName));

        // Add info to Extent report
        // Create PASS result in Test Management Tool (Jira)
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        LoggerUtils.log.info(("[TEST FAILED] " + sTestName));
        WebDriver driver = getWebDriverInstance(result);
        Assert.assertNotNull(driver, "Driver Instance is NULL!");

        // Add info to Extent report
        // Create FAIL result in Test Management Tool (Jira)
        // Open TA bug in Bug Management Tool (Jira) - add error message, add stack trace, attach screenshot
        // Open Ticket in Task Management Tool (Jira) and assign to test owner
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        LoggerUtils.log.info(("[TEST SKIPPED" + sTestName));
    }

    private static WebDriver getWebDriverInstance(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        WebDriver driver = null;
        try {
            driver = (WebDriver) result.getTestClass().getRealClass().getDeclaredField("driver").get(result.getInstance());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            LoggerUtils.log.warn("Cannot get Driver instance for test '" + sTestName + "! Message: " + e.getMessage());
        }
        return driver;
    }

}
