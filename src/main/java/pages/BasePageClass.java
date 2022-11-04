package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utils.LoggerUtils;
import utils.PropertiesUtils;
import utils.WebDriverUtils;

import java.time.Duration;

public abstract class BasePageClass extends LoggerUtils {

    protected WebDriver driver;

    private static final String BASE_URL = PropertiesUtils.getBaseUrl();

    protected BasePageClass(WebDriver driver) {
        Assert.assertFalse(WebDriverUtils.hasDriverQuit(driver), "Driver instance has quit!");
        this.driver = driver;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    protected void openUrl(String url) {
        log.trace("openUrl(" + url + ")");
        driver.get(url);
    }

    protected String getPageUrl(String sPath) {
        log.trace("getPageUrl(" + sPath + ")");
        return getBaseUrl() + sPath;
    }

    private WebDriverWait getWebDriverWait(int timeout) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }

    protected WebElement getWebElement(By locator) {
        log.trace("getWebElement(" + locator + ")");
        return driver.findElement(locator);
    }

    protected WebElement getWebElement(By locator, int timeout) {
        log.trace("getWebElement(" + locator + ", " + timeout + ")");
        WebDriverWait wait = getWebDriverWait(timeout);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected WebElement waitForWebElementToBeClickable(WebElement element, int timeout) {
        log.trace("waitForWebElementToBeClickable(" + element + ", " + timeout + ")");
        WebDriverWait wait = getWebDriverWait(timeout);
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void typeTextToWebElement(WebElement element, String text) {
        log.trace("typeTextToWebElement(" + element + ", " + text + ")");
        element.sendKeys(text);
    }

    protected void clearAndTypeTextToWebElement(WebElement element, String text) {
        log.trace("clearAndTypeTextToWebElement(" + element + ", " + text + ")");
        element.clear();
        element.sendKeys(text);
    }

    protected void clickOnWebElement(WebElement element) {
        log.trace("clickOnWebElement(" + element + ")");
        element.click();
    }

    protected void clickOnWebElement(WebElement element, int timeout) {
        log.trace("clickOnWebElement(" + element + ", " + timeout + ")");
        WebElement webElement = waitForWebElementToBeClickable(element, timeout);
        webElement.click();
    }

    protected void clickOnWebElementJS(WebElement element) {
        log.trace("clickOnWebElementJS(" + element + ")");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }

    protected void clickOnWebElementJS(WebElement element, int timeout) {
        log.trace("clickOnWebElement(" + element + ", " + timeout + ")");
        WebElement webElement = waitForWebElementToBeClickable(element, timeout);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", webElement);
    }

    protected String getTextFromWebElement(WebElement element) {
        log.trace("getTextFromWebElement(" + element + ")");
        return element.getText();
    }

    protected String getAttributeFromWebElement(WebElement element, String attribute) {
        log.trace("getAttributeFromWebElement(" + element + ", " + attribute + ")");
        return element.getAttribute(attribute);
    }

    protected String getValueFromWebElement(WebElement element) {
        log.trace("getAttributeFromWebElement(" + element + ")");
        return getAttributeFromWebElement(element, "value");
    }

    protected String getValueFromWebElementJS(WebElement element) {
        log.trace("getValueFromWebElementJS(" + element + ")");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (String) js.executeScript("return arguments[0].value", element);
    }

    protected boolean isWebElementDisplayed(By locator) {
        log.trace("isWebElementDisplayed(" + locator + ")");
        try {
            WebElement element = getWebElement(locator);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isWebElementDisplayed(WebElement element) {
        log.trace("isWebElementDisplayed(" + element + ")");
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isWebElementEnabled(WebElement element) {
        log.trace("isWebElementEnabled(" + element + ")");
        return element.isEnabled();
    }

    protected boolean waitForUrlChange(String url, int timeout) {
        log.trace("waitForUrlChange(" + url + ", " + timeout + ")");
        WebDriverWait wait = getWebDriverWait(timeout);
        return wait.until(ExpectedConditions.urlContains(url));
    }

    protected boolean waitForUrlChangeToExactUrl(String url, int timeout) {
        log.trace("waitForUrlChangeToExactUrl(" + url + ", " + timeout + ")");
        WebDriverWait wait = getWebDriverWait(timeout);
        return wait.until(ExpectedConditions.urlToBe(url));
    }

    protected boolean waitUntilPageIsReady(int timeout) {
        log.trace("getPageUrl(" + timeout + ")");
        WebDriverWait wait = getWebDriverWait(timeout);
        return wait.until(driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete")
        );

    }
}
