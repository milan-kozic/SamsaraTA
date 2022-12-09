package pages;

import data.Time;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class HeroDetailsDialogBox extends BasePageClass {

    // Locators
    private final String heroDetailsDialogBoxString = "//div[@id='heroModal']";
    private final By heroDetailsDialogBoxLocator = By.id("heroModal");
    private final By heroDetailsDialogBoxTitleLocator = By.xpath(heroDetailsDialogBoxString + "//h4[contains(@class,'modal-title')]");
    private final By closeButtonLocator = By.xpath(heroDetailsDialogBoxString + "/button[contains(@class,'btn-default')]");

    // Constructor
    public HeroDetailsDialogBox(WebDriver driver) {
        super(driver);
        log.trace("new HeroDetailsDialogBox()");
    }

    private boolean isHeroDetailsDialogBoxOpened(int timeout) {
        return isWebElementVisible(heroDetailsDialogBoxLocator, timeout);
    }

    private boolean isHeroDetailsDialogBoxClosed(int timeout) {
        return isWebElementInvisible(heroDetailsDialogBoxLocator, timeout);
    }

    public HeroDetailsDialogBox verifyHeroDetailsDialogBox() {
        log.debug("verifyHeroDetailsDialogBox()");
        waitUntilPageIsReady(Time.TIME_SHORTER);
        Assert.assertTrue(isHeroDetailsDialogBoxOpened(Time.TIME_SHORTER), "'Hero Details' DialogBox is NOT opened!");
        return this;
    }

    public boolean isDialogBoxTitleDisplayed() {
        log.debug("isDialogBoxTitleDisplayed()");
        return isWebElementDisplayed(heroDetailsDialogBoxTitleLocator);
    }

    public String getDialogBoxTitle() {
        log.debug("getDialogBoxTitle()");
        Assert.assertTrue(isDialogBoxTitleDisplayed(), "DialogBox Title is NOT displayed on 'Hero Details' DialogBox");
        WebElement heroDetailsDialogBoxTitle = getWebElement(heroDetailsDialogBoxTitleLocator);
        return getTextFromWebElement(heroDetailsDialogBoxTitle);
    }

    public boolean isCloseButtonDisplayed() {
        log.debug("isCloseButtonDisplayed()");
        return isWebElementDisplayed(closeButtonLocator);
    }

    public HeroesPage clickCloseButton() {
        log.debug("clickCloseButton()");
        WebElement closeButton = getWebElement(closeButtonLocator);
        clickOnWebElement(closeButton);
        Assert.assertTrue(isHeroDetailsDialogBoxClosed(Time.TIME_SHORTER), "'Hero Details' DialogBox is NOT closed!");
        HeroesPage heroesPage = new HeroesPage(driver);
        return heroesPage.verifyHeroesPage();
    }

}
