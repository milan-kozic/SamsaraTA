package pages;

import data.PageUrlPaths;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class CommonLoggedOutPage extends BasePageClass {

    // Locators
    private final String headerLocatorString = "header#headContainer";
    private final By loginLinkLocator = By.cssSelector(headerLocatorString + " a[href='" + PageUrlPaths.LOGIN_PAGE + "']");
    private final By samsaraLogoLocator = By.cssSelector(headerLocatorString + " a.navbar-brand");

    // Constructor
    protected CommonLoggedOutPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage clickSamsaraLogo() {
        log.debug("clickSamsaraLogo()");
        WebElement samsaraLogo = getWebElement(samsaraLogoLocator);
        clickOnWebElement(samsaraLogo);
        LoginPage loginPage = new LoginPage(driver);
        return loginPage.verifyLoginPage();
    }

    public LoginPage clickLoginLink() {
        log.debug("clickLoginLink()");
        WebElement loginLink = getWebElement(loginLinkLocator);
        clickOnWebElement(loginLink);
        LoginPage loginPage = new LoginPage(driver);
        return loginPage.verifyLoginPage();
    }
}
