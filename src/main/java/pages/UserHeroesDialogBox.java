package pages;

import data.Time;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class UserHeroesDialogBox extends BasePageClass {

    // Locators
    private final String userHeroesDialogBoxLocatorString = "//div[@id='heroesModal']";
    private final By userHeroesDialogBoxLocator = By.id("heroesModal");
    private final By userHeroesDialogBoxTitleLocator = By.xpath(userHeroesDialogBoxLocatorString + "//h4[contains(@class, 'modal-title')]");
    private final By closeButtonLocator = By.xpath(userHeroesDialogBoxLocatorString + "//button[contains(@class,'btn-default')]");

    // Constructor
    public UserHeroesDialogBox(WebDriver driver) {
        super(driver);
    }

    public UserHeroesDialogBox verifyUserHeroesDialogBox() {
        log.debug("verifyUserHeroesDialogBox()");
        Assert.assertTrue(isUserHeroesDialogBoxOpened(Time.TIME_SHORTER), "'User Heroes' DialogBox is NOT opened!");
        waitUntilPageIsReady(Time.TIME_SHORTER);
        return this;
    }

    private boolean isUserHeroesDialogBoxOpened(int timeout) {
        return isWebElementVisible(userHeroesDialogBoxLocator, timeout);
    }

    private boolean isUserHeroesDialogBoxClosed(int timeout) {
        return isWebElementInvisible(userHeroesDialogBoxLocator, timeout);
    }

    public boolean isDialogBoxTitleDisplayed() {
        log.debug("isDialogBoxTitleDisplayed()");
        return isWebElementDisplayed(userHeroesDialogBoxTitleLocator);
    }

    public String getDialogBoxTitle() {
        log.debug("getDialogBoxTitle()");
        Assert.assertTrue(isDialogBoxTitleDisplayed(), "'User Heroes' DialogBox Title is NOT displayed!");
        WebElement dialogBoxTitle = getWebElement(userHeroesDialogBoxTitleLocator);
        return getTextFromWebElement(dialogBoxTitle);
    }

    public boolean isCloseButtonDisplayed() {
        log.debug("isCloseButtonDisplayed()");
        return isWebElementDisplayed(closeButtonLocator);
    }

    public UsersPage clickCloseButton() {
        log.debug("clickCloseButton()");
        Assert.assertTrue(isCloseButtonDisplayed(), "Close Button is NOT displayed on 'User Heroes' DialogBox!");
        WebElement closeButton = getWebElement(closeButtonLocator);
        clickOnWebElement(closeButton);
        Assert.assertTrue(isUserHeroesDialogBoxClosed(Time.TIME_SHORTER), "'User Heroes' DialogBox is NOT closed!");
        UsersPage usersPage = new UsersPage(driver);
        return usersPage.verifyUsersPage();
    }
}
