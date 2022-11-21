package pages;

import data.PageUrlPaths;
import data.Time;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class UsersPage extends CommonLoggedInPage {

    // Page Url Path
    private final String USERS_PAGE_URL = getPageUrl(PageUrlPaths.USERS_PAGE);

    // Locators
    private final By addNewUserButtonLocator = By.xpath("//a[contains(@class, 'btn-info') and contains(@onclick, 'openAddUserModal')]");

    // Constructor
    public UsersPage(WebDriver driver) {
        super(driver);
        log.trace("new UsersPage()");
    }

    public UsersPage open() {
        return open(true);
    }

    public UsersPage open(boolean bVerify) {
        log.debug("Open UsersPage (" + USERS_PAGE_URL + ")");
        openUrl(USERS_PAGE_URL);
        if (bVerify) {
            verifyUsersPage();
        }
        return this;
    }

    public UsersPage verifyUsersPage() {
        log.debug("verifyUsersPage()");
        waitForUrlChange(USERS_PAGE_URL, Time.TIME_SHORTER);
        waitUntilPageIsReady(Time.TIME_SHORT);
        return this;
    }

    public boolean isAddNewUserButtonDisplayed() {
        log.debug("isAddNewUserButtonDisplayed()");
        return isWebElementDisplayed(addNewUserButtonLocator);
    }


    public AddUserDialogBox clickAddNewUserButton() {
        log.debug("clickAddNewUserButton()");
        Assert.assertTrue(isAddNewUserButtonDisplayed(), "Add New User Button is NOT displayed on Users Page!");
        WebElement addNewUserButton = getWebElement(addNewUserButtonLocator);
        clickOnWebElement(addNewUserButton);
        AddUserDialogBox addUserDialogBox = new AddUserDialogBox(driver);
        return addUserDialogBox.verifyAddUserDialogBox();
    }
}
