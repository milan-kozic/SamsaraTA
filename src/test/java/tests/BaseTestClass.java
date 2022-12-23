package tests;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import utils.LoggerUtils;
import utils.PropertiesUtils;
import utils.ScreenShotUtils;
import utils.WebDriverUtils;

public class BaseTestClass extends LoggerUtils {

    protected WebDriver setUpDriver() {
        return WebDriverUtils.setUpDriver();
    }

    protected void quitDriver(WebDriver driver) {
        WebDriverUtils.quitDriver(driver);
    }

    // For "One Test Class -> Multiple Test Methods" approach
    protected void tearDown(WebDriver driver, boolean bSuccess) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement methodTrace = stackTrace[2];
        String sTestName = methodTrace.getClassName() + "." + methodTrace.getMethodName();
        if (PropertiesUtils.getTakeScreenshot()) {
            if (!bSuccess) {
                ScreenShotUtils.takeScreenShot(driver, sTestName);
            }
        }
        quitDriver(driver);
    }

    // For "One Test Class -> One Test Method" approach
    protected void tearDown(WebDriver driver, ITestResult result) {
        tearDown(driver, result, 0);
    }

    protected void tearDown(WebDriver driver, ITestResult result, int session) {
        String sTestName = result.getTestClass().getName();
        session = Math.abs(session);
        if (session > 0) {
            sTestName = sTestName + "_" + session;
        }
        try {
            ifFailed(driver, result, session);
        } catch (AssertionError | Exception e) {
            log.error("Exception occurred in tearDown(" + sTestName + ")! Message: " + e.getMessage());
        } finally {
            quitDriver(driver);
        }
    }

    private void ifFailed(WebDriver driver, ITestResult result, int session) {
        String sTestName = result.getTestClass().getName();
        session = Math.abs(session);
        if (session > 0) {
            sTestName = sTestName + "_" + session;
        }
        if (result.getStatus() == ITestResult.FAILURE) {
            if (PropertiesUtils.getTakeScreenshot() && !getListenerTakeScreenShot(result)) {
                // log.info("Taking ScreenShot from BaseTestClass");
                ScreenShotUtils.takeScreenShot(driver, sTestName);
            }
        }
    }

    private boolean getListenerTakeScreenShot(ITestResult result) {
        try {
            return (boolean) result.getTestContext().getAttribute("listenerTakeScreenShot");
        } catch (Exception e) {
            return false;
        }
    }
}
