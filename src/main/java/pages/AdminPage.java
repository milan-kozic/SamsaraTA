package pages;

import data.PageUrlPaths;
import data.Time;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

public class AdminPage extends CommonLoggedInPage {

    // Page Url Path
    private final String ADMIN_PAGE_URL = getPageUrl(PageUrlPaths.ADMIN_PAGE);

    // Page Factory Locators
    // @CacheLookup
    @FindBy(id = "usersAllowed")
    WebElement allowUsersToShareRegistrationCodeCheckBox;

    // Constructor
    public AdminPage(WebDriver driver) {
        super(driver);
        log.trace("new AdminPage()");
    }

    public AdminPage open() {
        return open(true);
    }

    public AdminPage open(boolean bVerify) {
        log.debug("Open AdminPage (" + ADMIN_PAGE_URL + ")");
        openUrl(ADMIN_PAGE_URL);
        if (bVerify) {
            verifyAdminPage();
        }
        return this;
    }

    public AdminPage verifyAdminPage() {
        log.debug("verifyAdminPage()");
        waitForUrlChange(ADMIN_PAGE_URL, Time.TIME_SHORTER);
        waitUntilPageIsReady(Time.TIME_SHORT);
        return this;
    }

    public boolean isAllowUsersToShareRegistrationCodeCheckBoxDisplayed() {
        log.debug("isAllowUsersToShareRegistrationCodeCheckBoxDisplayed()");
        return isWebElementDisplayed(allowUsersToShareRegistrationCodeCheckBox);
    }

    public boolean isAllowUsersToShareRegistrationCodeCheckBoxChecked() {
        log.debug("isAllowUsersToShareRegistrationCodeCheckBoxDisplayed()");
        Assert.assertTrue(isAllowUsersToShareRegistrationCodeCheckBoxDisplayed(), "'Allow Users To Share Registration Code' CheckBox is NOT displayed on Admin Page!");
        return isWebElementSelected(allowUsersToShareRegistrationCodeCheckBox);
    }

    public AdminPage checkAllowUsersToShareRegistrationCode() {
        log.debug("checkAllowUsersToShareRegistrationCode()");
        if (!isAllowUsersToShareRegistrationCodeCheckBoxChecked()) {
            clickOnWebElement(allowUsersToShareRegistrationCodeCheckBox);
        }
        AdminPage adminPage = new AdminPage(driver);
        return adminPage.verifyAdminPage();
    }

    public AdminPage uncheckAllowUsersToShareRegistrationCode() {
        log.debug("uncheckAllowUsersToShareRegistrationCode()");
        if (isAllowUsersToShareRegistrationCodeCheckBoxChecked()) {
            clickOnWebElement(allowUsersToShareRegistrationCodeCheckBox);
        }
        AdminPage adminPage = new AdminPage(driver);
        return adminPage.verifyAdminPage();
    }
}
