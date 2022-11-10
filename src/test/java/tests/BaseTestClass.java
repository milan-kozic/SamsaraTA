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
        String sTestName = result.getName();
        if (PropertiesUtils.getTakeScreenshot()) {
            if (result.getStatus() == ITestResult.FAILURE) {
                ScreenShotUtils.takeScreenShot(driver, sTestName);
            }
        }
        quitDriver(driver);
    }
}
