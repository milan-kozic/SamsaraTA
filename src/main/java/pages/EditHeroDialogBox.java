package pages;

import data.Time;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

public class EditHeroDialogBox extends BasePageClass {

    private final String editHeroDialogBoxString = "//div[@id='editHeroModal']";

    // Page Factory Locators
    @FindBy(id = "editHeroModal")
    private WebElement editHeroDialogBox;

    @FindBy(xpath = editHeroDialogBoxString + "//h4[contains(@class,'modal-title')]")
    private WebElement editHeroDialogBoxTitle;

    @FindBy(xpath = editHeroDialogBoxString + "/button[contains(@class,'btn-default')]")
    private WebElement cancelButton;

    // Constructor
    public EditHeroDialogBox(WebDriver driver) {
        super(driver);
        log.trace("new EditHeroDialogBox()");
    }

    private boolean isEditHeroDialogBoxOpened(int timeout) {
        return isWebElementVisible(editHeroDialogBox, timeout);
    }

    private boolean isEditHeroDialogBoxClosed(int timeout) {
        return isWebElementInvisible(editHeroDialogBox, timeout);
    }

    public EditHeroDialogBox verifyEditHeroDialogBox() {
        log.debug("verifyEditHeroDialogBox()");
        waitUntilPageIsReady(Time.TIME_SHORTER);
        Assert.assertTrue(isEditHeroDialogBoxOpened(Time.TIME_SHORTER), "'Edit Hero' DialogBox is NOT opened!");
        return this;
    }

    public boolean isDialogBoxTitleDisplayed() {
        log.debug("isDialogBoxTitleDisplayed()");
        return isWebElementDisplayed(editHeroDialogBoxTitle);
    }

    public String getDialogBoxTitle() {
        log.debug("getDialogBoxTitle()");
        Assert.assertTrue(isDialogBoxTitleDisplayed(), "DialogBox Title is NOT displayed on 'Edit Hero' DialogBox");
        return getTextFromWebElement(editHeroDialogBoxTitle);
    }

    public boolean isCancelButtonDisplayed() {
        log.debug("isCancelButtonDisplayed()");
        return isWebElementDisplayed(cancelButton);
    }

    public HeroesPage clickCancelButton() {
        log.debug("clickCancelButton()");
        Assert.assertTrue(isCancelButtonDisplayed(), "Cancel Button is NOT displayed on 'Edit Hero' DialogBox!");
        clickOnWebElement(cancelButton);
        Assert.assertTrue(isEditHeroDialogBoxClosed(Time.TIME_SHORTER), "'Edit Hero' DialogBox is NOT closed!");
        HeroesPage heroesPage = new HeroesPage(driver);
        return heroesPage.verifyHeroesPage();
    }
}
