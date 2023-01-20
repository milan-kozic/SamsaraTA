package pages;

import data.Time;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import utils.DateTimeUtils;

import java.util.Date;

public class UserDetailsDialogBox extends BasePageClass{

    // Locators
    private final String userDetailsDialogBoxLocatorString = "//div[@id='userModal']";

    @FindBy(id = "userModal")
    private WebElement userDetailsDialogBox;

    @FindBy(xpath = userDetailsDialogBoxLocatorString + "//h4[contains(@class,'modal-title')]")
    private WebElement userDetailsDialogBoxTitle;

    @FindBy(xpath = userDetailsDialogBoxLocatorString + "//span[@class='username']")
    private WebElement usernameText;

    @FindBy(xpath = userDetailsDialogBoxLocatorString + "//span[@class='firstName']")
    private WebElement firstNameText;

    @FindBy(xpath = userDetailsDialogBoxLocatorString + "//span[@class='lastName']")
    private WebElement lastNameText;

    @FindBy(xpath = userDetailsDialogBoxLocatorString + "//span[@class='about']")
    private WebElement aboutText;

    @FindBy(xpath = userDetailsDialogBoxLocatorString + "//span[@class='created']")
    private WebElement createdAtText;

    @FindBy(xpath = userDetailsDialogBoxLocatorString + "//button[contains(@class,'btn-default')]")
    private WebElement closeButton;

    // Constructor
    public UserDetailsDialogBox(WebDriver driver) {
        super(driver);
    }

    public UserDetailsDialogBox verifyUserDetailsDialogBox() {
        log.debug("verifyUserDetailsDialogBox()");
        Assert.assertTrue(isUserDetailsDialogBoxOpened(Time.TIME_SHORTER), "'User Details' DialogBox is NOT opened!");
        waitUntilPageIsReady(Time.TIME_SHORTER);
        return this;
    }

    private boolean isUserDetailsDialogBoxOpened(int timeout) {
        return isWebElementVisible(userDetailsDialogBox, timeout);
    }

    private boolean isUserDetailsDialogBoxClosed(int timeout) {
        return isWebElementInvisible(userDetailsDialogBox, timeout);
    }

    public boolean isDialogBoxTitleDisplayed() {
        log.debug("isDialogBoxTitleDisplayed()");
        return isWebElementDisplayed(userDetailsDialogBoxTitle);
    }

    public String getDialogBoxTitle() {
        log.debug("getDialogBoxTitle()");
        Assert.assertTrue(isDialogBoxTitleDisplayed(), "'User Details' DialogBox Title is NOT displayed!");
        return getTextFromWebElement(userDetailsDialogBoxTitle);
    }

    public boolean isUsernameTextDisplayed() {
        log.debug("isUsernameTextDisplayed()");
        return isWebElementDisplayed(usernameText);
    }

    public String getUsername() {
        log.debug("getUsername()");
        Assert.assertTrue(isUsernameTextDisplayed(), "'Username' Text is NOT displayed on 'User Details' DialogBox");
        return getTextFromWebElement(usernameText);
    }

    public boolean isFirstNameTextDisplayed() {
        log.debug("isFirstNameTextDisplayed()");
        return isWebElementDisplayed(firstNameText);
    }

    public String getFirstName() {
        log.debug("getFirstName()");
        Assert.assertTrue(isFirstNameTextDisplayed(), "'First Name' Text is NOT displayed on 'User Details' DialogBox");
        return getTextFromWebElement(firstNameText);
    }

    public boolean isLastNameTextDisplayed() {
        log.debug("isLastNameTextDisplayed()");
        return isWebElementDisplayed(lastNameText);
    }

    public String getLastName() {
        log.debug("getLastName()");
        Assert.assertTrue(isLastNameTextDisplayed(), "'Last Name' Text is NOT displayed on 'User Details' DialogBox");
        return getTextFromWebElement(lastNameText);
    }

    public boolean isAboutTextDisplayed() {
        log.debug("isAboutTextDisplayed()");
        return isWebElementDisplayed(aboutText);
    }

    public String getAbout() {
        log.debug("getAbout()");
        Assert.assertTrue(isAboutTextDisplayed(), "'About' Text is NOT displayed on 'User Details' DialogBox");
        return getTextFromWebElement(aboutText);
    }

    public boolean isCreatedAtTextDisplayed() {
        log.debug("isCreatedAtTextDisplayed()");
        return isWebElementDisplayed(createdAtText);
    }

    private String getCreatedAtText() {
        log.debug("getCreatedAtText()");
        Assert.assertTrue(isCreatedAtTextDisplayed(), "'Created At' Date is NOT displayed on 'User Details' DialogBox");
        return getTextFromWebElement(createdAtText);
    }

    public Date getCreatedAtDate() {
        log.debug("getCreatedAtDate()");
        String sDateTime = getCreatedAtText();
        String sBrowserTimeZone = DateTimeUtils.getBrowserTimeZone(driver);
        sDateTime = sDateTime + " " + sBrowserTimeZone;
        return DateTimeUtils.getParsedDateTime(sDateTime, "dd.MM.yyyy. HH:mm z");
    }

    public boolean isCloseButtonDisplayed() {
        log.debug("isCloseButtonDisplayed()");
        return isWebElementDisplayed(closeButton);
    }

    private void clickCloseButton() {
        log.debug("clickCloseButton()");
        Assert.assertTrue(isCloseButtonDisplayed(), "Close Button is NOT displayed on 'User Details' DialogBox!");
        clickOnWebElement(closeButton);
        Assert.assertTrue(isUserDetailsDialogBoxClosed(Time.TIME_SHORTER), "'User Details' DialogBox is NOT closed!");
    }

    public UsersPage clickCloseButtonToUsersPage() {
        log.debug("clickCloseButtonToUsersPage()");
        clickCloseButton();
        UsersPage usersPage = new UsersPage(driver);
        return usersPage.verifyUsersPage();
    }

    public HeroesPage clickCloseButtonToHeroesPage() {
        log.debug("clickCloseButtonToUsersPage()");
        clickCloseButton();
        HeroesPage heroesPage = new HeroesPage(driver);
        return heroesPage.verifyHeroesPage();
    }
}
