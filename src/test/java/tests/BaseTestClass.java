package tests;

import org.openqa.selenium.WebDriver;
import utils.LoggerUtils;
import utils.WebDriverUtils;

public class BaseTestClass extends LoggerUtils {

    protected WebDriver setUpDriver() {
        return WebDriverUtils.setUpDriver();
    }

    protected void quitDriver(WebDriver driver) {
        WebDriverUtils.quitDriver(driver);
    }

    // tearDown
}
