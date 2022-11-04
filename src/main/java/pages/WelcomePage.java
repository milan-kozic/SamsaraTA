package pages;

import data.PageUrlPaths;
import data.Time;
import org.openqa.selenium.WebDriver;

public class WelcomePage extends CommonLoggedInPage {

    // Page Url
    private final String WELCOME_PAGE_URL = getPageUrl(PageUrlPaths.WELCOME_PAGE);

    // Constructor
    public WelcomePage(WebDriver driver) {
        super(driver);
        log.trace("new WelcomePage()");
    }

    public WelcomePage open() {
        return open(true);
    }

    public WelcomePage open(boolean bVerify) {
        log.debug("Open WelcomePage (" + WELCOME_PAGE_URL + ")");
        openUrl(WELCOME_PAGE_URL);
        if(bVerify) {
            verifyWelcomePage();
        }
        return this;
    }

    public WelcomePage verifyWelcomePage() {
        log.debug("verifyWelcomePage()");
        waitForUrlChangeToExactUrl(WELCOME_PAGE_URL, Time.TIME_SHORTER);
        waitUntilPageIsReady(Time.TIME_SHORT);
        return this;
    }
}
