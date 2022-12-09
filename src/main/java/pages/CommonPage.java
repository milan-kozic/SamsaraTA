package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public abstract class CommonPage extends BasePageClass {

    // Locators
    private final By pageTitleLocator = By.xpath("//div[contains(@class, 'panel-title')]");

    // Constructor
    protected CommonPage(WebDriver driver) {
        super(driver);
    }

    public boolean isPageTitleDisplayed() {
        log.debug("isPageTitleDisplayed()");
        return isWebElementDisplayed(pageTitleLocator);
    }

    public String getPageTitle() {
        log.debug("getPageTitle()");
        Assert.assertTrue(isPageTitleDisplayed(), "Page Title is NOT displayed!");
        WebElement pageTitle = getWebElement(pageTitleLocator);
        return getTextFromWebElement(pageTitle);
    }
}
