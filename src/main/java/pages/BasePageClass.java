package pages;

import data.Time;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utils.LoggerUtils;
import utils.PropertiesUtils;
import utils.WebDriverUtils;

import java.time.Duration;
import java.util.List;

public abstract class BasePageClass extends LoggerUtils {

    protected WebDriver driver;

    private static final String BASE_URL = PropertiesUtils.getBaseUrl();

    // Constructor
    protected BasePageClass(WebDriver driver) {
        Assert.assertFalse(WebDriverUtils.hasDriverQuit(driver), "Driver instance has quit!");
        this.driver = driver;
        PageFactory.initElements(driver, this);
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

    protected String getCurrentUrl() {
        log.trace("getCurrentUrl()");
        return driver.getCurrentUrl();
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

    protected WebElement getNestedWebElement(WebElement element, By locator) {
        log.trace("getNestedWebElement(" + element + ", " + locator + ")");
        return element.findElement(locator);
    }

    protected WebElement getNestedWebElement(WebElement element, By locator, int timeout) {
        log.trace("getNestedWebElement(" + element + ", " + locator + ", " + timeout + ")");
        WebDriverWait wait = getWebDriverWait(timeout);
        return wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(element, locator));
    }

    protected List<WebElement> getWebElements(By locator) {
        log.trace("getWebElements(" + locator + ")");
        return driver.findElements(locator);
    }

    protected List<WebElement> getNestedWebElements(WebElement element, By locator) {
        log.trace("getNestedWebElements(" + element + ", " + locator + ")");
        return element.findElements(locator);
    }

    protected WebElement waitForWebElementToBeClickable(WebElement element, int timeout) {
        log.trace("waitForWebElementToBeClickable(" + element + ", " + timeout + ")");
        WebDriverWait wait = getWebDriverWait(timeout);
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected WebElement waitForWebElementToBeVisible(By locator, int timeout) {
        log.trace("waitForWebElementTobeVisible(" + locator + ", " + timeout + ")");
        WebDriverWait wait = getWebDriverWait(timeout);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForWebElementToBeVisible(WebElement element, int timeout) {
        log.trace("waitForWebElementTobeVisible(" + element + ", " + timeout + ")");
        WebDriverWait wait = getWebDriverWait(timeout);
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected boolean waitForWebElementToBeInvisible(By locator, int timeout) {
        log.trace("waitForWebElementTobeVisible(" + locator + ", " + timeout + ")");
        WebDriverWait wait = getWebDriverWait(timeout);
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected boolean waitForWebElementToBeInvisible(WebElement element, int timeout) {
        log.trace("waitForWebElementTobeVisible(" + element + ", " + timeout + ")");
        WebDriverWait wait = getWebDriverWait(timeout);
        return wait.until(ExpectedConditions.invisibilityOf(element));
    }

    protected boolean waitForWebElementToBeSelected(WebElement element, int timeout) {
        log.trace("waitForWebElementToBeSelected(" + element + ", " + timeout + ")");
        WebDriverWait wait = getWebDriverWait(timeout);
        return wait.until(ExpectedConditions.elementToBeSelected(element));
    }

    protected boolean isWebElementVisible(By locator, int timeout) {
        log.trace("isWebElementVisible(" + locator + ", " + timeout + ")");
        try {
            WebElement element = waitForWebElementToBeVisible(locator, timeout);
            return element != null;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isWebElementVisible(WebElement element, int timeout) {
        log.trace("isWebElementVisible(" + element + ", " + timeout + ")");
        try {
            WebElement webElement = waitForWebElementToBeVisible(element, timeout);
            return webElement != null;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isWebElementInvisible(By locator, int timeout) {
        log.trace("isWebElementInvisible(" + locator + ", " + timeout + ")");
        try {
            return waitForWebElementToBeInvisible(locator, timeout);
        } catch (Exception e) {
            return true;
        }
    }

    protected boolean isWebElementInvisible(WebElement element, int timeout) {
        log.trace("isWebElementInvisible(" + element + ", " + timeout + ")");
        try {
            return waitForWebElementToBeInvisible(element, timeout);
        } catch (Exception e) {
            return false;
        }
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

    protected boolean isWebElementDisplayed(WebElement element, int timeout) {
        log.trace("isWebElementDisplayed(" + element + ", " + timeout + ")");
        try {
            WebDriverUtils.setImplicitWait(driver, timeout);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        } finally {
            WebDriverUtils.setImplicitWait(driver, Time.IMPLICIT_TIMEOUT);
        }
    }

    protected boolean isNestedWebElementDisplayed(WebElement element, By locator) {
        log.trace("isNestedWebElementDisplayed(" + element + ", " + locator + ")");
        try {
            WebElement webElement = getNestedWebElement(element, locator);
            return webElement.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isWebElementEnabled(WebElement element) {
        log.trace("isWebElementEnabled(" + element + ")");
        return element.isEnabled();
    }

    protected boolean isWebElementEnabled(WebElement element, int timeout) {
        log.trace("isWebElementEnabled(" + element + ", " + timeout + ")");
        try {
            WebElement webElement = waitForWebElementToBeClickable(element, timeout);
            return webElement != null;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isWebElementSelected(WebElement element) {
        log.trace("isWebElementSelected(" + element + ")");
        return element.isSelected();
    }

    protected boolean isWebElementSelected(WebElement element, int timeout) {
        log.trace("isWebElementSelected(" + element + ", " + timeout + ")");
        try {
            return waitForWebElementToBeSelected(element, timeout);
        } catch (Exception e) {
            return false;
        }
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
