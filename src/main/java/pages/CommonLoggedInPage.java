package pages;

import data.PageUrlPaths;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public abstract class CommonLoggedInPage extends BasePageClass {

    // Locators
    private final String headerLocatorString = "//header[@id='headContainer']";
    private final By usersTabLocator = By.xpath(headerLocatorString + "//a[@href='" + PageUrlPaths.USERS_PAGE + "']");
    private final By logoutLinkLocator = By.xpath(headerLocatorString + "//a[contains(@href, 'logoutForm.submit')]");

    // Constructor
    public CommonLoggedInPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLogoutLinkDisplayed() {
        log.debug("isLogoutLinkDisplayed()");
        return isWebElementDisplayed(logoutLinkLocator);
    }

    public LoginPage clickLogoutLink() {
        log.debug("clickLogoutLink()");
        Assert.assertTrue(isLogoutLinkDisplayed(), "Logout Link is NOT displayed on Navigation Bar!");
        WebElement logoutLink = getWebElement(logoutLinkLocator);
        clickOnWebElement(logoutLink);
        LoginPage loginPage = new LoginPage(driver);
        return loginPage.verifyLoginPage();
    }
}
