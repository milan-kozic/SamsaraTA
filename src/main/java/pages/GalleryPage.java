package pages;

import data.PageUrlPaths;
import data.Time;
import org.openqa.selenium.WebDriver;

public class GalleryPage extends CommonLoggedInPage {

    // Page Url Path
    private final String GALLERY_PAGE_URL = getPageUrl(PageUrlPaths.GALLERY_PAGE);

    // Constructor
    public GalleryPage(WebDriver driver) {
        super(driver);
        log.trace("new GalleryPage()");
    }

    public GalleryPage open() {
        return open(true);
    }

    public GalleryPage open(boolean bVerify) {
        log.debug("Open GalleryPage (" + GALLERY_PAGE_URL + ")");
        openUrl(GALLERY_PAGE_URL);
        if (bVerify) {
            verifyGalleryPage();
        }
        return this;
    }

    public GalleryPage verifyGalleryPage() {
        log.debug("verifyGalleryPage()");
        waitForUrlChange(GALLERY_PAGE_URL, Time.TIME_SHORTER);
        waitUntilPageIsReady(Time.TIME_SHORT);
        return this;
    }
}
