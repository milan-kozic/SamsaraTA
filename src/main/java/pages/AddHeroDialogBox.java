package pages;

import data.Time;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

public class AddHeroDialogBox extends BasePageClass {


    private final String addHeroDialogBoxString = "//div[@id='addHeroModal']";

    // Page Factory Locators
    @FindBy(id = "addHeroModal")
    private WebElement addHeroDialogBox;

    @FindBy(xpath = addHeroDialogBoxString + "//h4[contains(@class,'modal-title')]")
    private WebElement addHeroDialogBoxTitle;

    @FindBy(xpath = addHeroDialogBoxString + "//button[contains(@class,'btn-default')]")
    private WebElement cancelButton;

    // Constructor
    public AddHeroDialogBox(WebDriver driver) {
        super(driver);
        log.trace("new AddHeroDialogBox()");
    }

    private boolean isAddHeroDialogBoxOpened(int timeout) {
        return isWebElementVisible(addHeroDialogBox, timeout);
    }

    private boolean isAddHeroDialogBoxClosed(int timeout) {
        return isWebElementInvisible(addHeroDialogBox, timeout);
    }

    public AddHeroDialogBox verifyAddHeroDialogBox() {
        log.debug("verifyAddHeroDialogBox()");
        waitUntilPageIsReady(Time.TIME_SHORTER);
        Assert.assertTrue(isAddHeroDialogBoxOpened(Time.TIME_SHORTER), "'Add Hero' DialogBox is NOT opened!");
        return this;
    }

    public boolean isDialogBoxTitleDisplayed() {
        log.debug("isDialogBoxTitleDisplayed()");
        return isWebElementDisplayed(addHeroDialogBoxTitle);
    }

    public String getDialogBoxTitle() {
        log.debug("getDialogBoxTitle()");
        Assert.assertTrue(isDialogBoxTitleDisplayed(), "DialogBox Title is NOT displayed on 'Add Hero' DialogBox");
        return getTextFromWebElement(addHeroDialogBoxTitle);
    }

    public boolean isCancelButtonDisplayed() {
        log.debug("isCancelButtonDisplayed()");
        return isWebElementDisplayed(cancelButton);
    }

    private void clickCancelButton() {
        Assert.assertTrue(isCancelButtonDisplayed(), "Cancel Button is NOT displayed on 'Add Hero' DialogBox!");
        clickOnWebElement(cancelButton);
        Assert.assertTrue(isAddHeroDialogBoxClosed(Time.TIME_SHORTER), "'Add Hero' DialogBox is NOT closed!");
    }

    public HeroesPage clickCancelButtonToHeroesPage() {
        log.debug("clickCancelButtonToHeroesPage()");
        clickCancelButton();
        HeroesPage heroesPage = new HeroesPage(driver);
        return heroesPage.verifyHeroesPage();
    }
    public UsersPage clickCancelButtonToUsersPage() {
        log.debug("clickCancelButtonToUsersPage()");
        clickCancelButton();
        UsersPage usersPage = new UsersPage(driver);
        return usersPage.verifyUsersPage();
    }
}
