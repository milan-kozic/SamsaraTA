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

    @FindBy(id = "name")
    private WebElement heroNameTextField;

    @FindBy(id = "level")
    private WebElement heroLevelTextField;

    @FindBy(id = "type")
    private WebElement heroClassDropDownList;

    @FindBy(xpath = addHeroDialogBoxString + "//button[contains(@class,'btn-default')]")
    private WebElement cancelButton;

    @FindBy(id = "submitButton")
    private WebElement saveButton;

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

    public boolean isHeroNameTextFieldDisplayed() {
        log.debug("isHeroNameTextFieldDisplayed()");
        return isWebElementDisplayed(heroNameTextField);
    }

    public AddHeroDialogBox typeHeroName(String sHeroName) {
        log.debug("typeHeroName(" + sHeroName + ")");
        Assert.assertTrue(isHeroNameTextFieldDisplayed(), "'Hero Name' TextField is NOT displayed on 'Add Hero' DialogBox");
        clearAndTypeTextToWebElement(heroNameTextField, sHeroName);
        return this;
    }

    public boolean isHeroLevelTextFieldDisplayed() {
        log.debug("isHeroLevelTextFieldDisplayed()");
        return isWebElementDisplayed(heroLevelTextField);
    }

    public AddHeroDialogBox typeHeroLevel(String sHeroLevel) {
        log.debug("typeHeroLevel(" + sHeroLevel + ")");
        Assert.assertTrue(isHeroLevelTextFieldDisplayed(), "'Hero Level' TextField is NOT displayed on 'Add Hero' DialogBox");
        clearAndTypeTextToWebElement(heroLevelTextField, sHeroLevel);
        return this;
    }

    public AddHeroDialogBox typeHeroLevel(int iHeroLevel) {
        String sHeroLevel = String.valueOf(iHeroLevel);
        return typeHeroLevel(sHeroLevel);
    }

    public boolean isHeroClassDropDownListDisplayed() {
        log.debug("isHeroClassDropDownListDisplayed()");
        return isWebElementDisplayed(heroClassDropDownList);
    }

    public String getSelectedHeroClass() {
        log.debug("getSelectedHeroClass()");
        Assert.assertTrue(isHeroClassDropDownListDisplayed(), "'Hero Class' DropDown List is NOT displayed on 'Add Hero' DialogBox");
        return getFirstSelectedOptionOnWebElement(heroClassDropDownList);
    }

    public boolean isHeroClassPresent(String sHeroClass) {
        log.debug("isHeroClassPresent(" + sHeroClass + ")");
        Assert.assertTrue(isHeroClassDropDownListDisplayed(), "'Hero Class' DropDown List is NOT displayed on 'Add Hero' DialogBox");
        return isOptionPresentOnWebElement(heroClassDropDownList, sHeroClass);
    }

    public AddHeroDialogBox selectHeroClass(String sHeroClass) {
        log.debug("selectHeroClass(" + sHeroClass + ")");
        Assert.assertTrue(isHeroClassPresent(sHeroClass), "Option '" + sHeroClass + "' is NOT present in 'Hero Class' DropDown List!");
        selectOptionOnWebElement(heroClassDropDownList, sHeroClass);
        return this;
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

    public boolean isSaveButtonDisplayed() {
        log.debug("isSaveButtonDisplayed()");
        return isWebElementDisplayed(saveButton);
    }

    public boolean isSaveButtonEnabled() {
        log.debug("isSaveButtonEnabled()");
        Assert.assertTrue(isSaveButtonDisplayed(), "Save Button is NOT displayed on 'Add Hero' DialogBox!");
        return isWebElementEnabled(saveButton);
    }

    public HeroesPage clickSaveButton() {
        log.debug("clickSaveButton()");
        Assert.assertTrue(isSaveButtonEnabled(), "Save Button is NOT enabled!");
        clickOnWebElement(saveButton, Time.TIME_SHORTEST);
        Assert.assertTrue(isAddHeroDialogBoxClosed(Time.TIME_SHORTER), "'Add Hero' DialogBox is NOT closed!");
        HeroesPage heroesPage = new HeroesPage(driver);
        return heroesPage.verifyHeroesPage();
    }
}
