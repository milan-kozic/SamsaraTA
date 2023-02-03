package pages;

import data.PageUrlPaths;
import data.Time;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.ashot.Screenshot;
import utils.ScreenShotUtils;

import java.awt.image.BufferedImage;

public class ProfilePage extends CommonLoggedInPage{

    // Page Url Path
    private final String PROFILE_PAGE_URL = getPageUrl(PageUrlPaths.PROFILE_PAGE);

    // Locators
    private final By profileImageLocator = By.id("profile-img");

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

    public BufferedImage getProfileImageSnapShot() {
        log.debug("getProfileImageSnapShot()");
        WebElement profileImage = getWebElement(profileImageLocator);
        return ScreenShotUtils.takeSnapShotOfWebElement(driver, profileImage);
    }

    public Screenshot getProfileImageSnapShotAS() {
        log.debug("getProfileImageSnapShotAS()");
        WebElement profileImage = getWebElement(profileImageLocator);
        return ScreenShotUtils.takeSnapShotOfWebElementAS(driver, profileImage);
    }

    public ProfilePage saveProfileImageSnapShot() {
        log.debug("saveProfileImageSnapShot()");
        WebElement profileImage = getWebElement(profileImageLocator);
        ScreenShotUtils.saveSnapShotOfWebElement(driver, profileImage, "ProfileImage");
        return this;
    }

    public ProfilePage saveProfileImageSnapShotAS() {
        log.debug("saveProfileImageSnapShotAS()");
        WebElement profileImage = getWebElement(profileImageLocator);
        ScreenShotUtils.saveSnapShotOfWebElementAS(driver, profileImage, "ProfileImage");
        return this;
    }
}
