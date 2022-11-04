package pages;

import data.PageUrlPaths;
import data.Time;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class LoginPage extends CommonLoggedOutPage {

    // Page Url
    private final String LOGIN_PAGE_URL = getPageUrl(PageUrlPaths.LOGIN_PAGE);

    // Locators
    private final String loginBoxLocatorString = "//div[@id='loginbox']";
    private final By usernameTextFieldLocator = By.id("username");
    private final By passwordTextFieldLocator = By.id("password");
    private final By loginButtonLocator = By.xpath(loginBoxLocatorString + "//input[contains(@class,'btn-primary')]");
    // private final By loginButtonLocator = By.cssSelector("div#loginbox input.btn-primary");
    private final By successMessageLocator = By.xpath(loginBoxLocatorString + "//div[contains(@class, 'alert-success')]");


    // Bad Practice
    // WebElement usernameTextField = driver.findElement(usernameTextFieldLocator);

    // Constructor
    public LoginPage(WebDriver driver) {
        super(driver);
        log.trace("new LoginPage()");
    }

    public LoginPage open() {
        return open(true);
    }

    public LoginPage open(boolean bVerify) {
        log.debug("Open LoginPage (" + LOGIN_PAGE_URL + ")");
        openUrl(LOGIN_PAGE_URL);
        if(bVerify) {
            verifyLoginPage();
        }
        return this;
    }

    public LoginPage verifyLoginPage() {
        log.debug("verifyLoginPage()");
        waitForUrlChange(LOGIN_PAGE_URL, Time.TIME_SHORTER);
        waitUntilPageIsReady(Time.TIME_SHORT);
        return this;
    }

    public boolean isUsernameTextFieldDisplayed() {
        log.debug("isUsernameTextFieldDisplayed()");
        return isWebElementDisplayed(usernameTextFieldLocator);
    }

    public void typeUsername(String sUsername) {
        log.debug("typeUsername(" + sUsername + ")");
        Assert.assertTrue(isUsernameTextFieldDisplayed(), "Username Text Field is NOT present on Login Page!");
        WebElement usernameTextField = getWebElement(usernameTextFieldLocator);
        clearAndTypeTextToWebElement(usernameTextField, sUsername);
    }

    public String getUsername() {
        log.debug("getUsername()");
        Assert.assertTrue(isUsernameTextFieldDisplayed(), "Username Text Field is NOT present on Login Page!");
        WebElement usernameTextField = getWebElement(usernameTextFieldLocator);
        return getValueFromWebElement(usernameTextField);
    }

    public boolean isPasswordTextFieldDisplayed() {
        log.debug("isPasswordTextFieldDisplayed()");
        return isWebElementDisplayed(passwordTextFieldLocator);
    }

    public void typePassword(String sPassword) {
        log.debug("typePassword(" + sPassword + ")");
        Assert.assertTrue(isPasswordTextFieldDisplayed(), "Password Text Field is NOT present on Login Page!");
        WebElement passwordTextField = getWebElement(passwordTextFieldLocator);
        clearAndTypeTextToWebElement(passwordTextField, sPassword);
    }

    public String getPassword() {
        log.debug("getPassword()");
        Assert.assertTrue(isPasswordTextFieldDisplayed(), "Password Text Field is NOT present on Login Page!");
        WebElement passwordTextField = getWebElement(passwordTextFieldLocator);
        return getValueFromWebElement(passwordTextField);
    }

    public boolean isLoginButtonDisplayed() {
        log.debug("isLoginButtonDisplayed()");
        return isWebElementDisplayed(loginButtonLocator);
    }

    public boolean isLoginButtonEnabled() {
        log.debug("isLoginButtonEnabled()");
        Assert.assertTrue(isLoginButtonDisplayed(), "Login Button is NOT present on Login Page!");
        WebElement loginButton = getWebElement(loginButtonLocator);
        return isWebElementEnabled(loginButton);
    }

    public WelcomePage clickLoginButton() {
        log.debug("clickLoginButton()");
        Assert.assertTrue(isLoginButtonEnabled(), "Login Button is NOT enabled on Login Page!");
        WebElement loginButton = getWebElement(loginButtonLocator);
        clickOnWebElement(loginButton);
        WelcomePage welcomePage = new WelcomePage(driver);
        return welcomePage.verifyWelcomePage();
    }

    public boolean isSuccessMessageDisplayed() {
        log.debug("isSuccessMessageDisplayed()");
        return isWebElementDisplayed(successMessageLocator);
    }

    public String getSuccessMessage() {
        log.debug("getSuccessMessage()");
        Assert.assertTrue(isSuccessMessageDisplayed(), "Success Message is NOT enabled on Login Page!");
        WebElement successMessage = getWebElement(successMessageLocator);
        return getTextFromWebElement(successMessage);
    }

}
