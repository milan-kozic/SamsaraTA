package pages;

import data.Time;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class EditUserDialogBox extends BasePageClass {

    // Locators
    private final String editUserDialogBoxLocatorString = "//div[@id='editUserModal']";
    private final By editUserDialogBoxLocator = By.id("editUserModal");
    private final By editUserDialogBoxTitleLocator = By.xpath(editUserDialogBoxLocatorString + "//h4[contains(@class,'modal-title')]");
    private final By cancelButtonLocator = By.xpath(editUserDialogBoxLocatorString + "/button[contains(@class,'btn-default')]");

    // Constructor
    public EditUserDialogBox(WebDriver driver) {
        super(driver);
        log.trace("new EditUserDialogBox()");
    }

    private boolean isEditUserDialogBoxOpened(int timeout) {
        return isWebElementVisible(editUserDialogBoxLocator, timeout);
    }

    private boolean isEditUserDialogBoxClosed(int timeout) {
        return isWebElementInvisible(editUserDialogBoxLocator, timeout);
    }

    public EditUserDialogBox verifyEditUserDialogBox() {
        log.debug("verifyEditUserDialogBox()");
        waitUntilPageIsReady(Time.TIME_SHORTER);
        Assert.assertTrue(isEditUserDialogBoxOpened(Time.TIME_SHORTER), "'Edit User' DialogBox is NOT opened!");
        return this;
    }

    public boolean isDialogBoxTitleDisplayed() {
        log.debug("isDialogBoxTitleDisplayed()");
        return isWebElementDisplayed(editUserDialogBoxTitleLocator);
    }

    public String getDialogBoxTitle() {
        log.debug("getDialogBoxTitle()");
        Assert.assertTrue(isDialogBoxTitleDisplayed(), "DialogBox Title is NOT displayed on 'Edit User' DialogBox");
        WebElement editUserDialogBoxTitle = getWebElement(editUserDialogBoxTitleLocator);
        return getTextFromWebElement(editUserDialogBoxTitle);
    }

    public boolean isCancelButtonDisplayed() {
        log.debug("isCancelButtonDisplayed()");
        return isWebElementDisplayed(cancelButtonLocator);
    }

    public UsersPage clickCancelButton() {
        log.debug("clickCancelButton()");
        Assert.assertTrue(isCancelButtonDisplayed(), "Cancel Button is NOT displayed on 'Edit User' DialogBox!");
        WebElement cancelButton = getWebElement(cancelButtonLocator);
        clickOnWebElement(cancelButton);
        Assert.assertTrue(isEditUserDialogBoxClosed(Time.TIME_SHORTER), "'Edit User' DialogBox is NOT closed!");
        UsersPage usersPage = new UsersPage(driver);
        return usersPage.verifyUsersPage();
    }

}
