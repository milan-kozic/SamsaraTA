package pages;

import data.Time;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class DeleteHeroDialogBox extends BasePageClass {

    // Locators
    private final String deleteHeroDialogBoxString = "//div[@id='deleteHeroModal']";
    private final By deleteHeroDialogBoxLocator = By.id("deleteHeroModal");
    private final By deleteHeroDialogBoxTitleLocator = By.xpath(deleteHeroDialogBoxString + "//h4[contains(@class,'modal-title')]");
    private final By cancelButtonLocator = By.xpath(deleteHeroDialogBoxString + "//button[contains(@class,'btn-default')]");

    // Constructor
    public DeleteHeroDialogBox(WebDriver driver) {
        super(driver);
        log.trace("new DeleteHeroDialogBox()");
    }

    private boolean isDeleteHeroDialogBoxOpened(int timeout) {
        return isWebElementVisible(deleteHeroDialogBoxLocator, timeout);
    }

    private boolean isDeleteHeroDialogBoxClosed(int timeout) {
        return isWebElementInvisible(deleteHeroDialogBoxLocator, timeout);
    }

    public DeleteHeroDialogBox verifyDeleteHeroDialogBox() {
        log.debug("verifyDeleteHeroDialogBox()");
        waitUntilPageIsReady(Time.TIME_SHORTER);
        Assert.assertTrue(isDeleteHeroDialogBoxOpened(Time.TIME_SHORTER), "'Delete Hero' DialogBox is NOT opened!");
        return this;
    }

    public boolean isDialogBoxTitleDisplayed() {
        log.debug("isDialogBoxTitleDisplayed()");
        return isWebElementDisplayed(deleteHeroDialogBoxTitleLocator);
    }

    public String getDialogBoxTitle() {
        log.debug("getDialogBoxTitle()");
        Assert.assertTrue(isDialogBoxTitleDisplayed(), "DialogBox Title is NOT displayed on 'Delete Hero' DialogBox");
        WebElement deleteHeroDialogBoxTitle = getWebElement(deleteHeroDialogBoxTitleLocator);
        return getTextFromWebElement(deleteHeroDialogBoxTitle);
    }

    public boolean isCancelButtonDisplayed() {
        log.debug("isCancelButtonDisplayed()");
        return isWebElementDisplayed(cancelButtonLocator);
    }

    public HeroesPage clickCancelButton() {
        log.debug("clickCancelButton()");
        WebElement cancelButton = getWebElement(cancelButtonLocator);
        clickOnWebElement(cancelButton);
        Assert.assertTrue(isDeleteHeroDialogBoxClosed(Time.TIME_SHORTER), "'Delete Hero' DialogBox is NOT closed!");
        HeroesPage heroesPage = new HeroesPage(driver);
        return heroesPage.verifyHeroesPage();
    }

}
