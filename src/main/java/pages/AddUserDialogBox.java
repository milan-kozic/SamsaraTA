package pages;

import data.Time;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

public class AddUserDialogBox extends BasePageClass {

    // Locators
    private final String addUserDialogBoxLocatorString = "//div[@id='addUserModal']";
    private final By addUserDialogBoxLocator = By.id("addUserModal");
    private final By addUserDialogBoxTitleLocator = By.xpath(addUserDialogBoxLocatorString + "//h4[contains(@class,'modal-title')]");
    private final By cancelButtonLocator = By.xpath(addUserDialogBoxLocatorString + "//button[contains(@class,'btn-default')]");

    // Constructor
    public AddUserDialogBox(WebDriver driver) {
        super(driver);
    }

    public AddUserDialogBox verifyAddUserDialogBox() {
        log.debug("verifyAddUserDialogBox()");
        Assert.assertTrue(isAddUserDialogBoxOpened(Time.TIME_SHORTER), "'Add User' DialogBox is NOT opened!");
        waitUntilPageIsReady(Time.TIME_SHORTER);
        return this;
    }

    private boolean isAddUserDialogBoxOpened(int timeout) {
        return isWebElementVisible(addUserDialogBoxLocator, timeout);
    }

    private boolean isAddUserDialogBoxClosed(int timeout) {
        return isWebElementInvisible(addUserDialogBoxLocator, timeout);
    }

    public boolean isDialogBoxTitleDisplayed() {
        log.debug("isDialogBoxTitleDisplayed()");
        return isWebElementDisplayed(addUserDialogBoxTitleLocator);
    }

    public String getDialogBoxTitle() {
        log.debug("getDialogBoxTitle()");
        Assert.assertTrue(isDialogBoxTitleDisplayed(), "DialogBox Title is NOT displayed on 'Add Hero' DialogBox");
        WebElement dialogBoxTitle = getWebElement(addUserDialogBoxTitleLocator);
        return getTextFromWebElement(dialogBoxTitle);
    }

    public boolean isCancelButtonDisplayed() {
        log.debug("isCancelButtonDisplayed()");
        return isWebElementDisplayed(cancelButtonLocator);
    }

    public UsersPage clickCancelButton() {
        log.debug("clickCancelButton()");
        Assert.assertTrue(isCancelButtonDisplayed(), "Cancel Button is NOT displayed on 'Add User' DialogBox!");
        WebElement cancelButton = getWebElement(cancelButtonLocator);
        clickOnWebElement(cancelButton);
        Assert.assertTrue(isAddUserDialogBoxClosed(Time.TIME_SHORTER), "'Add User' DialogBox is NOT closed!");
        UsersPage usersPage = new UsersPage(driver);
        return usersPage.verifyUsersPage();
    }

}
