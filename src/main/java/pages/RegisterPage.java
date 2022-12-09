package pages;

import data.PageUrlPaths;
import data.Time;
import objects.User;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

public class RegisterPage extends CommonLoggedOutPage {

    // Page Url Path
    private final String REGISTER_PAGE_URL = getPageUrl(PageUrlPaths.REGISTER_PAGE);

    // Page Factory Locators
    @FindBy(id = "username")
    private WebElement usernameTextField;

    @FindBy(id = "firstName")
    private WebElement firstNameTextField;

    @FindBy(id = "lastName")
    private WebElement lastNameTextField;

    @FindBy(id = "email")
    private WebElement emailTextField;

    @FindBy(id = "about")
    private WebElement aboutTextField;

    @FindBy(id = "secretQuestion")
    private WebElement secretQuestionTextField;

    @FindBy(id = "secretAnswer")
    private WebElement secretAnswerTextField;

    @FindBy(id = "password")
    private WebElement passwordTextField;

    @FindBy(id = "repassword")
    private WebElement confirmPasswordTextField;

    @FindBy(id = "usernameMessage")
    private WebElement usernameErrorMessage;

    @FindBy(id = "firstNameMessage")
    private WebElement firstNameErrorMessage;

    @FindBy(id = "lastNameMessage")
    private WebElement lastNameErrorMessage;

    @FindBy(id = "emailMessage")
    private WebElement emailErrorMessage;

    @FindBy(id = "aboutMessage")
    private WebElement aboutErrorMessage;

    @FindBy(id = "questionMessage")
    private WebElement secretQuestionErrorMessage;

    @FindBy(id = "answerMessage")
    private WebElement secretAnswerErrorMessage;

    @FindBy(id = "passwordMessage")
    private WebElement passwordErrorMessage;

    @FindBy(id = "repasswordMessage")
    private WebElement confirmPasswordErrorMessage;

    @FindBy(id = "submitButton")
    private WebElement signUpButton;

    // Constructor
    public RegisterPage(WebDriver driver) {
        super(driver);
        log.trace("new RegisterPage()");
    }

    public RegisterPage open() {
        return open(true);
    }

    public RegisterPage open(boolean bVerify) {
        log.debug("Open RegisterPage (" + REGISTER_PAGE_URL + ")");
        openUrl(REGISTER_PAGE_URL);
        if (bVerify) {
            verifyRegisterPage();
        }
        return this;
    }

    public RegisterPage verifyRegisterPage() {
        log.debug("verifyRegisterPage()");
        waitForUrlChange(REGISTER_PAGE_URL, Time.TIME_SHORTER);
        waitUntilPageIsReady(Time.TIME_SHORT);
        return this;
    }

    public boolean isUsernameTextFieldDisplayed() {
        log.debug("isUsernameTextFieldDisplayed()");
        return isWebElementDisplayed(usernameTextField);
    }

    public RegisterPage typeUsername(String sUsername) {
        log.debug("typeUsername(" + sUsername + ")");
        Assert.assertTrue(isUsernameTextFieldDisplayed(), "Username Text Field is NOT present on Register Page!");
        clearAndTypeTextToWebElement(usernameTextField, sUsername);
        return this;
    }

    public String getUsername() {
        log.debug("getUsername()");
        Assert.assertTrue(isUsernameTextFieldDisplayed(), "Username Text Field is NOT present on Register Page!");
        return getValueFromWebElement(usernameTextField);
    }

    public boolean isFirstNameTextFieldDisplayed() {
        log.debug("isFirstNameTextFieldDisplayed()");
        return isWebElementDisplayed(firstNameTextField);
    }

    public RegisterPage typeFirstName(String sFirstName) {
        log.debug("typeFirstName(" + sFirstName + ")");
        Assert.assertTrue(isFirstNameTextFieldDisplayed(), "First Name Text Field is NOT displayed on Register Page");
        clearAndTypeTextToWebElement(firstNameTextField, sFirstName);
        return this;
    }

    public String getFirstName() {
        log.debug("getFirstName()");
        Assert.assertTrue(isFirstNameTextFieldDisplayed(), "First Name Text Field is NOT displayed on Register Page");
        return getValueFromWebElement(firstNameTextField);
    }

    public boolean isLastNameTextFieldDisplayed() {
        log.debug("isLastNameTextFieldDisplayed()");
        return isWebElementDisplayed(lastNameTextField);
    }

    public RegisterPage typeLastName(String sLastName) {
        log.debug("typeLastName(" + sLastName + ")");
        Assert.assertTrue(isLastNameTextFieldDisplayed(), "Last Name Text Field is NOT displayed on Register Page");
        clearAndTypeTextToWebElement(lastNameTextField, sLastName);
        return this;
    }

    public String getLastName() {
        log.debug("getLastName()");
        Assert.assertTrue(isLastNameTextFieldDisplayed(), "Last Name Text Field is NOT displayed on Register Page");
        return getValueFromWebElement(lastNameTextField);
    }

    public boolean isEmailTextFieldDisplayed() {
        log.debug("isEmailTextFieldDisplayed()");
        return isWebElementDisplayed(emailTextField);
    }

    public RegisterPage typeEmail(String sEmail) {
        log.debug("typeEmail(" + sEmail + ")");
        Assert.assertTrue(isEmailTextFieldDisplayed(), "Email Text Field is NOT displayed on Register Page");
        clearAndTypeTextToWebElement(emailTextField, sEmail);
        return this;
    }

    public String getEmail() {
        log.debug("getEmail()");
        Assert.assertTrue(isEmailTextFieldDisplayed(), "Email Text Field is NOT displayed on Login Page");
        return getValueFromWebElement(emailTextField);
    }

    public boolean isAboutTextFieldDisplayed() {
        log.debug("isAboutTextFieldDisplayed()");
        return isWebElementDisplayed(aboutTextField);
    }

    public RegisterPage typeAbout(String sAbout) {
        log.debug("typeAbout(" + sAbout + ")");
        Assert.assertTrue(isAboutTextFieldDisplayed(), "About Text Field is NOT displayed on Login Page");
        clearAndTypeTextToWebElement(aboutTextField, sAbout);
        return this;
    }

    public String getAbout() {
        log.debug("getAbout()");
        Assert.assertTrue(isAboutTextFieldDisplayed(), "About Text Field is NOT displayed on Login Page");
        return getValueFromWebElement(aboutTextField);
    }

    public boolean isSecretQuestionTextFieldDisplayed() {
        log.debug("isSecretQuestionTextFieldDisplayed()");
        return isWebElementDisplayed(secretQuestionTextField);
    }

    public RegisterPage typeSecretQuestion(String sSecretQuestion) {
        log.debug("typeSecretQuestion(" + sSecretQuestion + ")");
        Assert.assertTrue(isSecretQuestionTextFieldDisplayed(), "Secret Question Text Field is NOT displayed on Login Page");
        clearAndTypeTextToWebElement(secretQuestionTextField, sSecretQuestion);
        return this;
    }

    public String getSecretQuestion() {
        log.debug("getSecretQuestion()");
        Assert.assertTrue(isSecretQuestionTextFieldDisplayed(), "Secret Question Text Field is NOT displayed on Login Page");
        return getValueFromWebElement(secretQuestionTextField);
    }

    public boolean isSecretAnswerTextFieldDisplayed() {
        log.debug("isSecretAnswerTextFieldDisplayed()");
        return isWebElementDisplayed(secretAnswerTextField);
    }

    public RegisterPage typeSecretAnswer(String sSecretAnswer) {
        log.debug("typeSecretAnswer(" + sSecretAnswer + ")");
        Assert.assertTrue(isSecretAnswerTextFieldDisplayed(), "Secret Answer Text Field is NOT displayed on Login Page");
        clearAndTypeTextToWebElement(secretAnswerTextField, sSecretAnswer);
        return this;
    }

    public String getSecretAnswer() {
        log.debug("getSecretAnswer()");
        Assert.assertTrue(isSecretAnswerTextFieldDisplayed(), "Secret Answer Text Field is NOT displayed on Login Page");
        return getValueFromWebElement(secretAnswerTextField);
    }

    public boolean isPasswordTextFieldDisplayed() {
        log.debug("isPasswordTextFieldDisplayed()");
        return isWebElementDisplayed(passwordTextField);
    }

    public RegisterPage typePassword(String sPassword) {
        log.debug("typePassword(" + sPassword + ")");
        Assert.assertTrue(isPasswordTextFieldDisplayed(), "Password Text Field is NOT displayed on Login Page");
        clearAndTypeTextToWebElement(passwordTextField, sPassword);
        return this;
    }

    public String getPassword() {
        log.debug("getPassword()");
        Assert.assertTrue(isPasswordTextFieldDisplayed(), "Password Text Field is NOT displayed on Login Page");
        return getValueFromWebElement(passwordTextField);
    }

    public boolean isConfirmPasswordTextFieldDisplayed() {
        log.debug("isConfirmPasswordTextFieldDisplayed()");
        return isWebElementDisplayed(confirmPasswordTextField);
    }

    public RegisterPage typeConfirmPassword(String sPassword) {
        log.debug("typeConfirmPassword(" + sPassword + ")");
        Assert.assertTrue(isConfirmPasswordTextFieldDisplayed(), "Confirm Password Text Field is NOT displayed on Login Page");
        clearAndTypeTextToWebElement(confirmPasswordTextField, sPassword);
        return this;
    }

    public String getConfirmPassword() {
        log.debug("getConfirmPassword()");
        Assert.assertTrue(isConfirmPasswordTextFieldDisplayed(), "Confirm Password Text Field is NOT displayed on Login Page");
        return getValueFromWebElement(confirmPasswordTextField);
    }

    public boolean isSignUpButtonDisplayed() {
        log.debug("isSignUpButtonDisplayed()");
        return isWebElementDisplayed(signUpButton);
    }

    public boolean isSignUpButtonEnabled() {
        log.debug("isSignUpButtonEnabled()");
        Assert.assertTrue(isSignUpButtonDisplayed(), "'Sign Up' Button is NOT present on Register Page!");
        return isWebElementEnabled(signUpButton, Time.TIME_SHORT);
    }

    public LoginPage clickSignUpButton() {
        log.debug("clickSignUpButton()");
        Assert.assertTrue(isSignUpButtonEnabled(), "'Sign Up' Button is NOT enabled on Register Page!");
        clickOnWebElement(signUpButton);
        LoginPage loginPage = new LoginPage(driver);
        return loginPage.verifyLoginPage();
    }

    /**
     * Register User with provided user details
     * @param user {User}
     * @return {LoginPage} LoginPage
     */
    public LoginPage registerUser(User user) {
        typeUsername(user.getUsername());
        typeFirstName(user.getFirstName());
        typeLastName(user.getLastName());
        typeEmail(user.getEmail());
        typeAbout(user.getAbout());
        typeSecretQuestion(user.getSecretQuestion());
        typeSecretAnswer(user.getSecretAnswer());
        typePassword(user.getPassword());
        typeConfirmPassword(user.getPassword());
        return clickSignUpButton();
    }

}
