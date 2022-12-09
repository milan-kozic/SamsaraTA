package pages;

import data.PageUrlPaths;
import data.Time;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.List;

public class HeroesPage extends CommonLoggedInPage {

    // Page Url Path
    private final String HEROES_PAGE_URL = getPageUrl(PageUrlPaths.HEROES_PAGE);

    // Page Factory Locators
    @FindBy(xpath = "//a[contains(@class,'btn-info') and contains(@onclick,'openAddHeroModal')]")
    private WebElement addNewHeroButton;

    @FindBy(id = "search")
    private WebElement searchTextBox;

    @FindBy(xpath = "//form[@id='searchForm']//i[contains(@class,'glyphicon-search')]")
    private WebElement searchButton;

    @FindBy(id = "heroes-table")
    private WebElement heroesTable;

    @FindBy(xpath = "//table[@id='heroes-table']/tbody/tr")
    List<WebElement> heroesTableRows;

    private String createXpathForHeroNameInHeroesTable(String sHeroName) {
        return "./tbody/tr/td[1]/self::td[text()='" + sHeroName + "']";
    }

    private String createXpathForHeroClassInHeroesTable(String sHeroName) {
        return createXpathForHeroNameInHeroesTable(sHeroName) + "/following-sibling::td[1]";
    }

    private String createXpathForHeroLevelInHeroesTable(String sHeroName) {
        return createXpathForHeroNameInHeroesTable(sHeroName) + "/following-sibling::td[2]";
    }

    private String createXpathForUserInHeroesTable(String sHeroName) {
        return createXpathForHeroNameInHeroesTable(sHeroName) + "/following-sibling::td[3]";
    }

    private String createXpathForActionIconsInHeroesTable(String sHeroName) {
        return createXpathForHeroNameInHeroesTable(sHeroName) + "/following-sibling::td[4]";
    }

    private String createXpathForHeroDetailsIconInHeroesTable(String sHeroName) {
        return createXpathForActionIconsInHeroesTable(sHeroName) + "/a[contains(@class,'btn-info')]";
    }

    private String createXpathForEditHeroIconInHeroesTable(String sHeroName) {
        return createXpathForActionIconsInHeroesTable(sHeroName) + "a[contains(@class,'btn-success')]";
    }

    private String createXpathForDeleteHeroIconInHeroesTable(String sHeroName) {
        return createXpathForActionIconsInHeroesTable(sHeroName) + "/a[contains(@class,'btn-danger')]";
    }

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

    public boolean isAddNewHeroButtonDisplayed() {
        log.debug("isAddNewHeroButtonDisplayed()");
        return isWebElementDisplayed(addNewHeroButton);
    }

    public AddHeroDialogBox clickAddNewHeroButton() {
        log.debug("clickAddNewHeroButton()");
        Assert.assertTrue(isAddNewHeroButtonDisplayed(), "'Add New Hero' Button is NOT displayed on Heroes Page");
        clickOnWebElement(addNewHeroButton);
        AddHeroDialogBox addHeroDialogBox =  new AddHeroDialogBox(driver);
        return addHeroDialogBox.verifyAddHeroDialogBox();
    }

    public String getAddNewHeroButtonTitle() {
        log.debug("getAddNewHeroButtonTitle()");
        Assert.assertTrue(isAddNewHeroButtonDisplayed(), "'Add New Hero' Button is NOT displayed on Heroes Page");
        return getTextFromWebElement(addNewHeroButton);
    }

    public boolean isHeroPresentInHeroesTable(String sHeroName) {
        log.debug("isHeroPresentInHeroesTable(" + sHeroName + ")");
        String xPath = createXpathForHeroNameInHeroesTable(sHeroName);
        return isNestedWebElementDisplayed(heroesTable, By.xpath(xPath));
    }

    public String getHeroClassInHeroesTable(String sHeroName) {
        log.debug("getHeroClassInHeroesTable(" + sHeroName + ")");
        Assert.assertTrue(isHeroPresentInHeroesTable(sHeroName), "Hero '" + sHeroName + "' is NOT present in Heroes Table!");
        String xPath = createXpathForHeroClassInHeroesTable(sHeroName);
        WebElement className = getNestedWebElement(heroesTable, By.xpath(xPath));
        return getTextFromWebElement(className);
    }

    public int getHeroLevelInHeroesTable(String sHeroName) {
        log.debug("getHeroLevelInHeroesTable(" + sHeroName + ")");
        Assert.assertTrue(isHeroPresentInHeroesTable(sHeroName), "Hero '" + sHeroName + "' is NOT present in Heroes Table!");
        String xPath = createXpathForHeroLevelInHeroesTable(sHeroName);
        WebElement level = getNestedWebElement(heroesTable, By.xpath(xPath));
        return Integer.parseInt(getTextFromWebElement(level));
    }

    private WebElement getUserLinkWebElementInHeroesTable(String sHeroName) {
        Assert.assertTrue(isHeroPresentInHeroesTable(sHeroName), "Hero '" + sHeroName + "' is NOT present in Heroes Table!");
        String xPath = createXpathForUserInHeroesTable(sHeroName);
        return getNestedWebElement(heroesTable, By.xpath(xPath));
    }

    public String getUserInHeroesTable(String sHeroName) {
        log.debug("getUserInHeroesTable(" + sHeroName + ")");
        WebElement userLink = getUserLinkWebElementInHeroesTable(sHeroName);
        return getTextFromWebElement(userLink);
    }

    public UserDetailsDialogBox clickUserLinkInHeroesTable(String sHeroName) {
        log.debug("clickUserLinkInHeroesTable(" + sHeroName + ")");
        WebElement userLink = getUserLinkWebElementInHeroesTable(sHeroName);
        clickOnWebElement(userLink);
        UserDetailsDialogBox userDetailsDialogBox = new UserDetailsDialogBox(driver);
        return userDetailsDialogBox.verifyUserDetailsDialogBox();
    }

    public boolean isHeroDetailsIconPresentInHeroesTable(String sHeroName) {
        log.debug("isHeroDetailsIconPresentInHeroesTable(" + sHeroName + ")");
        Assert.assertTrue(isHeroPresentInHeroesTable(sHeroName), "Hero '" + sHeroName + "' is NOT present in Heroes Table!");
        String xPath = createXpathForHeroDetailsIconInHeroesTable(sHeroName);
        return isNestedWebElementDisplayed(heroesTable, By.xpath(xPath));
    }

    public HeroDetailsDialogBox clickHeroDetailsIconInHeroesTable(String sHeroName) {
        log.debug("clickHeroDetailsIconInHeroesTable(" + sHeroName + ")");
        Assert.assertTrue(isHeroDetailsIconPresentInHeroesTable(sHeroName), "'Hero Details' Icon is NOT present in Heroes Table for Hero '" + sHeroName + "'!");
        String xPath = createXpathForHeroDetailsIconInHeroesTable(sHeroName);
        WebElement heroDetailsIcon = getNestedWebElement(heroesTable, By.xpath(xPath));
        clickOnWebElement(heroDetailsIcon);
        HeroDetailsDialogBox heroDetailsDialogBox = new HeroDetailsDialogBox(driver);
        return heroDetailsDialogBox.verifyHeroDetailsDialogBox();
    }

    public boolean isEditHeroIconPresentInHeroesTable(String sHeroName) {
        log.debug("isEditHeroIconPresentInHeroesTable(" + sHeroName + ")");
        Assert.assertTrue(isHeroPresentInHeroesTable(sHeroName), "Hero '" + sHeroName + "' is NOT present in Heroes Table!");
        String xPath = createXpathForEditHeroIconInHeroesTable(sHeroName);
        return isNestedWebElementDisplayed(heroesTable, By.xpath(xPath));
    }

    public EditHeroDialogBox clickEditHeroIconInHeroesTable(String sHeroName) {
        log.debug("clickEditHeroIconInHeroesTable(" + sHeroName + ")");
        Assert.assertTrue(isEditHeroIconPresentInHeroesTable(sHeroName), "'Edit Hero' Icon is NOT present in Heroes Table for Hero '" + sHeroName + "'!");
        String xPath = createXpathForEditHeroIconInHeroesTable(sHeroName);
        WebElement editHeroIcon = getNestedWebElement(heroesTable, By.xpath(xPath));
        clickOnWebElement(editHeroIcon);
        EditHeroDialogBox editHeroDialogBox = new EditHeroDialogBox(driver);
        return editHeroDialogBox.verifyEditHeroDialogBox();
    }

    public boolean isDeleteHeroIconPresentInHeroesTable(String sHeroName) {
        log.debug("isDeleteHeroIconPresentInHeroesTable(" + sHeroName + ")");
        Assert.assertTrue(isHeroPresentInHeroesTable(sHeroName), "Hero '" + sHeroName + "' is NOT present in Heroes Table!");
        String xPath = createXpathForDeleteHeroIconInHeroesTable(sHeroName);
        return isNestedWebElementDisplayed(heroesTable, By.xpath(xPath));
    }

    public DeleteHeroDialogBox clickDeleteHeroIconInHeroesTable(String sHeroName) {
        log.debug("clickDeleteHeroIconInHeroesTable(" + sHeroName + ")");
        Assert.assertTrue(isDeleteHeroIconPresentInHeroesTable(sHeroName), "'Delete Hero' Icon is NOT present in Heroes Table for Hero '" + sHeroName + "'!");
        String xPath = createXpathForDeleteHeroIconInHeroesTable(sHeroName);
        WebElement deleteHeroIcon = getNestedWebElement(heroesTable, By.xpath(xPath));
        clickOnWebElement(deleteHeroIcon);
        DeleteHeroDialogBox deleteHeroDialogBox = new DeleteHeroDialogBox(driver);
        return deleteHeroDialogBox.verifyDeleteHeroDialogBox();
    }


}
