package pages;

import data.Time;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class DeleteUserDialogBox extends BasePageClass {

    // Locators
    private final String deleteUserDialogBoxString = "//div[@id='deleteUserModal']";
    private final By deleteUserDialogBoxLocator = By.id("deleteUserModal");
    private final By deleteUserDialogBoxTitleLocator = By.xpath(deleteUserDialogBoxString + "//h4[contains(@class,'modal-title')]");
    private final By cancelButtonLocator = By.xpath(deleteUserDialogBoxString + "//button[contains(@class,'btn-default')]");

    // Constructor
    public DeleteUserDialogBox(WebDriver driver) {
        super(driver);
        log.trace("new DeleteUserDialogBox()");
    }

    private boolean isDeleteUserDialogBoxOpened(int timeout) {
        return isWebElementVisible(deleteUserDialogBoxLocator, timeout);
    }

    private boolean isDeleteUserDialogBoxClosed(int timeout) {
        return isWebElementInvisible(deleteUserDialogBoxLocator, timeout);
    }

    public DeleteUserDialogBox verifyDeleteUserDialogBox() {
        log.debug("verifyEditUserDialogBox()");
        waitUntilPageIsReady(Time.TIME_SHORTER);
        Assert.assertTrue(isDeleteUserDialogBoxOpened(Time.TIME_SHORTER), "'Delete User' DialogBox is NOT opened!");
        return this;
    }

    public boolean isDialogBoxTitleDisplayed() {
        log.debug("isDialogBoxTitleDisplayed()");
        return isWebElementDisplayed(deleteUserDialogBoxTitleLocator);
    }

    public String getDialogBoxTitle() {
        log.debug("getDialogBoxTitle()");
        Assert.assertTrue(isDialogBoxTitleDisplayed(), "DialogBox Title is NOT displayed on 'Delete User' DialogBox");
        WebElement deleteUserDialogBoxTitle = getWebElement(deleteUserDialogBoxTitleLocator);
        return getTextFromWebElement(deleteUserDialogBoxTitle);
    }

    public boolean isCancelButtonDisplayed() {
        log.debug("isCancelButtonDisplayed()");
        return isWebElementDisplayed(cancelButtonLocator);
    }

    public UsersPage clickCancelButton() {
        log.debug("clickCancelButton()");
        Assert.assertTrue(isCancelButtonDisplayed(), "Cancel Button is NOT displayed on 'Delete User' DialogBox!");
        WebElement cancelButton = getWebElement(cancelButtonLocator);
        clickOnWebElement(cancelButton);
        Assert.assertTrue(isDeleteUserDialogBoxClosed(Time.TIME_SHORTER), "'Delete User' DialogBox is NOT closed!");
        UsersPage usersPage = new UsersPage(driver);
        return usersPage.verifyUsersPage();
    }

}
