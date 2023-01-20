package pages;

import data.PageUrlPaths;
import data.Time;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PracticePage extends CommonLoggedInPage {

    // Page Url Path
    private final String PRACTICE_PAGE_URL = getPageUrl(PageUrlPaths.PRACTICE_PAGE);

    // Locators
    @FindBy(xpath = "//div[@id='useless-tooltip']/p[contains(@class,'h4 heading')]")
    private WebElement uselessTooltipTitle;

    @FindBy(id = "useless-tooltip-text")
    WebElement uselessTooltip;

    // Constructor
    public PracticePage(WebDriver driver) {
        super(driver);
        log.trace("new PracticePage()");
    }

    public PracticePage open() {
        return open(true);
    }

    public PracticePage open(boolean bVerify) {
        log.debug("Open PracticePage (" + PRACTICE_PAGE_URL + ")");
        openUrl(PRACTICE_PAGE_URL);
        if (bVerify) {
            verifyPracticePage();
        }
        return this;
    }

    public PracticePage verifyPracticePage() {
        log.debug("verifyPracticePage()");
        waitForUrlChange(PRACTICE_PAGE_URL, Time.TIME_SHORTER);
        waitUntilPageIsReady(Time.TIME_SHORT);
        return this;
    }
}
