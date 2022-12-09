package pages;

import data.Time;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class UserDetailsDialogBox extends BasePageClass{

    // Locators
    private final String userDetailsDialogBoxLocatorString = "//div[@id='userModal']";
    private final By userDetailsDialogBoxLocator = By.id("userModal");
    private final By userDetailsDialogBoxTitleLocator = By.xpath(userDetailsDialogBoxLocatorString + "//h4[contains(@class, 'modal-title')]");
    private final By closeButtonLocator = By.xpath(userDetailsDialogBoxLocatorString + "//button[contains(@class,'btn-default')]");

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
        return isWebElementVisible(userDetailsDialogBoxLocator, timeout);
    }

    private boolean isUserDetailsDialogBoxClosed(int timeout) {
        return isWebElementInvisible(userDetailsDialogBoxLocator, timeout);
    }

    public boolean isDialogBoxTitleDisplayed() {
        log.debug("isDialogBoxTitleDisplayed()");
        return isWebElementDisplayed(userDetailsDialogBoxTitleLocator);
    }

    public String getDialogBoxTitle() {
        log.debug("getDialogBoxTitle()");
        Assert.assertTrue(isDialogBoxTitleDisplayed(), "'User Details' DialogBox Title is NOT displayed!");
        WebElement dialogBoxTitle = getWebElement(userDetailsDialogBoxTitleLocator);
        return getTextFromWebElement(dialogBoxTitle);
    }

    public boolean isCloseButtonDisplayed() {
        log.debug("isCloseButtonDisplayed()");
        return isWebElementDisplayed(closeButtonLocator);
    }

    private void clickCloseButton() {
        log.debug("clickCloseButton()");
        Assert.assertTrue(isCloseButtonDisplayed(), "Close Button is NOT displayed on 'User Details' DialogBox!");
        WebElement closeButton = getWebElement(closeButtonLocator);
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
