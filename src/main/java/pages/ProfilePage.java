package pages;

import data.PageUrlPaths;
import data.Time;
import org.openqa.selenium.WebDriver;

public class ProfilePage extends CommonLoggedInPage{

    // Page Url Path
    private final String PROFILE_PAGE_URL = getPageUrl(PageUrlPaths.PROFILE_PAGE);

    // Constructor
    public ProfilePage(WebDriver driver) {
        super(driver);
        log.trace("new ProfilePage()");
    }

    public ProfilePage open() {
        return open(true);
    }

    public ProfilePage open(boolean bVerify) {
        log.debug("Open ProfilePage (" + PROFILE_PAGE_URL + ")");
        openUrl(PROFILE_PAGE_URL);
        if (bVerify) {
            verifyProfilePage();
        }
        return this;
    }

    public ProfilePage verifyProfilePage() {
        log.debug("verifyProfilePage()");
        waitForUrlChange(PROFILE_PAGE_URL, Time.TIME_SHORTER);
        waitUntilPageIsReady(Time.TIME_SHORT);
        return this;
    }
}
