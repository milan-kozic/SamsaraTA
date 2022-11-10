package pages;

import data.PageUrlPaths;
import data.Time;
import org.openqa.selenium.WebDriver;

public class AdminPage extends CommonLoggedInPage {

    // Page Url Path
    private final String ADMIN_PAGE_URL = getPageUrl(PageUrlPaths.ADMIN_PAGE);

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
}
