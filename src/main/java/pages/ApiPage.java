package pages;

import data.PageUrlPaths;
import data.Time;
import org.openqa.selenium.WebDriver;

public class ApiPage extends CommonLoggedInPage {

    // Page Url Path
    private final String API_PAGE_URL = getPageUrl(PageUrlPaths.API_PAGE);

    // Constructor
    public ApiPage(WebDriver driver) {
        super(driver);
        log.trace("new ApiPage()");
    }

    public ApiPage open() {
        return open(true);
    }

    public ApiPage open(boolean bVerify) {
        log.debug("Open ApiPage (" + API_PAGE_URL + ")");
        openUrl(API_PAGE_URL);
        if (bVerify) {
            verifyApiPage();
        }
        return this;
    }

    public ApiPage verifyApiPage() {
        log.debug("verifyApiPage()");
        waitForUrlChange(API_PAGE_URL, Time.TIME_SHORTER);
        waitUntilPageIsReady(Time.TIME_SHORT);
        return this;
    }
}
