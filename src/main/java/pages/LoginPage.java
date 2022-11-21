package pages;

import data.PageUrlPaths;
import data.Time;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class LoginPage extends CommonLoggedOutPage {

    // Page Url Path
    private final String LOGIN_PAGE_URL = getPageUrl(PageUrlPaths.LOGIN_PAGE);

    // Locators
    private final String loginBoxLocatorString = "//div[@id='loginbox']";
    private final By usernameTextFieldLocator = By.id("username");
    private final By passwordTextFieldLocator = By.id("password");
    private final By loginButtonLocator = By.xpath(loginBoxLocatorString + "//input[contains(@class,'btn-primary')]");
    private final By successMessageLocator = By.xpath(loginBoxLocatorString + "//div[contains(@class, 'alert-success')]");
    private final By errorMessageLocator = By.xpath(loginBoxLocatorString + "//div[contains(@class, 'alert-danger')]");
    private final By createAccountLinkLocator = By.xpath(loginBoxLocatorString + "//a[@href='" + PageUrlPaths.REGISTER_PAGE + "']");
    private final By resetPasswordLinkLocator = By.xpath(loginBoxLocatorString + "//a[@href='" + PageUrlPaths.RESET_PASSWORD_PAGE + "']");

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

    public LoginPage typeUsername(String sUsername) {
        log.debug("typeUsername(" + sUsername + ")");
        Assert.assertTrue(isUsernameTextFieldDisplayed(), "Username Text Field is NOT present on Login Page!");
        WebElement usernameTextField = getWebElement(usernameTextFieldLocator);
        clearAndTypeTextToWebElement(usernameTextField, sUsername);
        return this;
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

    public LoginPage typePassword(String sPassword) {
        log.debug("typePassword(" + sPassword + ")");
        Assert.assertTrue(isPasswordTextFieldDisplayed(), "Password Text Field is NOT present on Login Page!");
        WebElement passwordTextField = getWebElement(passwordTextFieldLocator);
        clearAndTypeTextToWebElement(passwordTextField, sPassword);
        return this;
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

    private void clickLoginButtonNoVerification() {
        Assert.assertTrue(isLoginButtonEnabled(), "Login Button is NOT displayed on Login Page!");
        WebElement loginButton = getWebElement(loginButtonLocator);
        clickOnWebElement(loginButton);
    }

    public WelcomePage clickLoginButton() {
        log.debug("clickLoginButton()");
        clickLoginButtonNoVerification();
        WelcomePage welcomePage = new WelcomePage(driver);
        return welcomePage.verifyWelcomePage();
    }

    public LoginPage clickLoginButtonNoProgress() {
        log.debug("clickLoginButtonNoProgress()");
        clickLoginButtonNoVerification();
        LoginPage loginPage = new LoginPage(driver);
        return loginPage.verifyLoginPage();
    }

    public boolean isSuccessMessageDisplayed() {
        log.debug("isSuccessMessageDisplayed()");
        return isWebElementDisplayed(successMessageLocator);
    }

    public String getSuccessMessage() {
        log.debug("getSuccessMessage()");
        Assert.assertTrue(isSuccessMessageDisplayed(), "Success Message is NOT displayed on Login Page!");
        WebElement successMessage = getWebElement(successMessageLocator);
        return getTextFromWebElement(successMessage);
    }

    public boolean isErrorMessageDisplayed() {
        log.debug("isErrorMessageDisplayed()");
        return isWebElementDisplayed(errorMessageLocator);
    }

    public String getErrorMessage() {
        log.debug("getErrorMessage()");
        Assert.assertTrue(isErrorMessageDisplayed(), "Error Message is NOT displayed on Login Page!");
        WebElement errorMessage = getWebElement(errorMessageLocator);
        return getTextFromWebElement(errorMessage);
    }

    public boolean isCreateAccountLinkDisplayed() {
        log.debug("isCreateAccountLinkDisplayed()");
        return isWebElementDisplayed(createAccountLinkLocator);
    }

    public RegisterPage clickCreateAccountLink() {
        log.debug("clickCreateAccountLink()");
        Assert.assertTrue(isCreateAccountLinkDisplayed(), "Create Account Link is NOT displayed on Login Page!");
        WebElement createAccountLink = getWebElement(createAccountLinkLocator);
        clickOnWebElement(createAccountLink);
        RegisterPage registerPage = new RegisterPage(driver);
        return registerPage.verifyRegisterPage();
    }

    public String getCreateAccountLinkTitle() {
        log.debug("getCreateAccountLinkTitle()");
        Assert.assertTrue(isCreateAccountLinkDisplayed(), "Create Account Link is NOT displayed on Login Page!");
        WebElement createAccountLink = getWebElement(createAccountLinkLocator);
        return getTextFromWebElement(createAccountLink);
    }

    public boolean isResetPasswordLinkDisplayed() {
        log.debug("isResetPasswordLinkDisplayed()");
        return isWebElementDisplayed(resetPasswordLinkLocator);
    }

    public ResetPasswordPage clickResetPasswordLink() {
        log.debug("clickResetPasswordLink()");
        Assert.assertTrue(isResetPasswordLinkDisplayed(), "Reset Password Link is NOT displayed on Login Page!");
        WebElement resetPasswordLink = getWebElement(resetPasswordLinkLocator);
        clickOnWebElement(resetPasswordLink);
        ResetPasswordPage resetPasswordPage = new ResetPasswordPage(driver);
        return resetPasswordPage.verifyResetPasswordPage();
    }

    public String getResetPasswordLinkTitle() {
        log.debug("getResetPasswordLinkTitle()");
        Assert.assertTrue(isResetPasswordLinkDisplayed(), "Reset Password Link is NOT displayed on Login Page!");
        WebElement resetPasswordLink = getWebElement(resetPasswordLinkLocator);
        return getTextFromWebElement(resetPasswordLink);
    }

    /**
     * Login to Samsara
     * @param sUsername - {String} Username
     * @param sPassword - {String} Password
     * @return {WelcomePage}
     */
    public WelcomePage login(String sUsername, String sPassword) {
        log.info("login(" + sUsername + ", " + sPassword + ")");
        typeUsername(sUsername);
        typePassword(sPassword);
        return clickLoginButton();
    }

}
