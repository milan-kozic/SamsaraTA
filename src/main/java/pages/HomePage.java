package pages;

import data.PageUrlPaths;
import data.Time;
import org.openqa.selenium.WebDriver;

public class HomePage extends CommonLoggedInPage {

    // Page Url Path
    private final String HOME_PAGE_URL = getPageUrl(PageUrlPaths.HOME_PAGE);

    // Constructor
    public HomePage(WebDriver driver) {
        super(driver);
        log.trace("new HomePage()");
    }

    public HomePage open() {
        return open(true);
    }

    public HomePage open(boolean bVerify) {
        log.debug("Open HomePage (" + HOME_PAGE_URL + ")");
        openUrl(HOME_PAGE_URL);
        if (bVerify) {
            verifyHomePage();
        }
        return this;
    }

    public HomePage verifyHomePage() {
        log.debug("verifyHomePage()");
        waitForUrlChange(HOME_PAGE_URL, Time.TIME_SHORTER);
        waitUntilPageIsReady(Time.TIME_SHORT);
        return this;
    }
}
