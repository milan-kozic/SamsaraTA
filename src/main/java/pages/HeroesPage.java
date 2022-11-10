package pages;

import data.PageUrlPaths;
import data.Time;
import org.openqa.selenium.WebDriver;

public class HeroesPage extends CommonLoggedInPage {

    // Page Url Path
    private final String HEROES_PAGE_URL = getPageUrl(PageUrlPaths.HEROES_PAGE);

    // Constructor
    public HeroesPage(WebDriver driver) {
        super(driver);
        log.trace("new HeroesPage()");
    }

    public HeroesPage open() {
        return open(true);
    }

    public HeroesPage open(boolean bVerify) {
        log.debug("Open HeroesPage (" + HEROES_PAGE_URL + ")");
        openUrl(HEROES_PAGE_URL);
        if (bVerify) {
            verifyHeroesPage();
        }
        return this;
    }

    public HeroesPage verifyHeroesPage() {
        log.debug("verifyHeroesPage()");
        waitForUrlChange(HEROES_PAGE_URL, Time.TIME_SHORTER);
        waitUntilPageIsReady(Time.TIME_SHORT);
        return this;
    }
}
