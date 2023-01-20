package pages;

import data.PageUrlPaths;
import data.Time;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.List;

public class UsersPage extends CommonLoggedInPage {

    // Page Url Path
    private final String USERS_PAGE_URL = getPageUrl(PageUrlPaths.USERS_PAGE);

    // Locators
    private final By addNewUserButtonLocator = By.xpath("//a[contains(@class, 'btn-info') and contains(@onclick, 'openAddUserModal')]");
    private final By usersTableLocator = By.id("users-table");
    private final By searchTextBoxLocator = By.id("search");
    private final By searchButtonLocator = By.xpath("//form[@id='searchForm']//i[contains(@class,'glyphicon-search')]");


    // //table[@id='users-table']//tbody//td[1]/self::td[text()='dedoje']/following-sibling::td[1]

    private String createXpathForUsernameInUsersTable(String sUsername) {
        return ".//tbody//td[1]/self::td[text()='" + sUsername + "']";
    }

    private String createXpathForDisplayNameInUsersTable(String sUsername) {
        return createXpathForUsernameInUsersTable(sUsername) + "/following-sibling::td[1]";
    }

    private String createXpathForHeroCountInUsersTable(String sUsername) {
        return createXpathForUsernameInUsersTable(sUsername) + "/following-sibling::td[2]";
    }

    private String createXpathForUserIconsInUsersTable(String sUsername) {
        return createXpathForUsernameInUsersTable(sUsername) + "/following-sibling::td[3]";
    }

    private String createXpathForUserDetailsIconInUsersTable(String sUsername) {
        return createXpathForUserIconsInUsersTable(sUsername) + "/a[contains(@class, 'btn-info')]";
    }

    private String createXpathForEditUserIconInUsersTable(String sUsername) {
        return createXpathForUserIconsInUsersTable(sUsername) + "/a[contains(@class, 'btn-success')]";
    }

    private String createXpathForDeleteUserIconInUsersTable(String sUsername) {
        return createXpathForUserIconsInUsersTable(sUsername) + "/a[contains(@class,'btn-danger')]";
    }

    // Constructor
    public UsersPage(WebDriver driver) {
        super(driver);
        log.trace("new UsersPage()");
    }

    public UsersPage open() {
        return open(true);
    }

    public UsersPage open(boolean bVerify) {
        log.debug("Open UsersPage (" + USERS_PAGE_URL + ")");
        openUrl(USERS_PAGE_URL);
        if (bVerify) {
            verifyUsersPage();
        }
        return this;
    }

    public UsersPage verifyUsersPage() {
        log.debug("verifyUsersPage()");
        waitForUrlChange(USERS_PAGE_URL, Time.TIME_SHORTER);
        waitUntilPageIsReady(Time.TIME_SHORT);
        return this;
    }

    public boolean isAddNewUserButtonDisplayed() {
        log.debug("isAddNewUserButtonDisplayed()");
        return isWebElementDisplayed(addNewUserButtonLocator);
    }

    public AddUserDialogBox clickAddNewUserButton() {
        log.debug("clickAddNewUserButton()");
        Assert.assertTrue(isAddNewUserButtonDisplayed(), "Add New User Button is NOT displayed on Users Page!");
        WebElement addNewUserButton = getWebElement(addNewUserButtonLocator);
        clickOnWebElement(addNewUserButton);
        AddUserDialogBox addUserDialogBox = new AddUserDialogBox(driver);
        return addUserDialogBox.verifyAddUserDialogBox();
    }

    public String getAddNewUserButtonTitle() {
        log.debug("getAddNewUserButtonTitle()");
        Assert.assertTrue(isAddNewUserButtonDisplayed(), "'Add New User' Button is NOT displayed on Users Page");
        WebElement addNewUserButton = getWebElement(addNewUserButtonLocator);
        return getTextFromWebElement(addNewUserButton);
    }

    public boolean isSearchTextBoxDisplayed() {
        log.debug("isSearchTextBoxDisplayed()");
        return isWebElementDisplayed(searchTextBoxLocator);
    }

    public UsersPage typeSearchText(String text) {
        log.debug("typeSearchText(" + text + ")");
        Assert.assertTrue(isSearchTextBoxDisplayed(), "'Search' Text Box is NOT displayed on Users Page");
        WebElement searchTextBox = getWebElement(searchTextBoxLocator);
        clearAndTypeTextToWebElement(searchTextBox, text);
        return this;
    }

    public String getSearchText() {
        log.debug("getSearchText()");
        Assert.assertTrue(isSearchTextBoxDisplayed(), "'Search' Text Box is NOT displayed on Users Page");
        WebElement searchTextBox = getWebElement(searchTextBoxLocator);
        return getValueFromWebElement(searchTextBox);
    }

    public boolean isSearchButtonDisplayed() {
        log.debug("isSearchButtonDisplayed()");
        return isWebElementDisplayed(searchButtonLocator);
    }

    public UsersPage clickSearchButton() {
        log.debug("clickSearchButton()");
        Assert.assertTrue(isSearchButtonDisplayed(), "'Search' Button is NOT displayed on Heroes Page");
        WebElement searchButton = getWebElement(searchButtonLocator);
        clickOnWebElement(searchButton);
        UsersPage usersPage = new UsersPage(driver);
        return usersPage.verifyUsersPage();
    }

    public UsersPage search(String sSearchText) {
        log.info("search(" + sSearchText + ")");
        typeSearchText(sSearchText);
        return clickSearchButton();
    }

    public int getNumberOfRowsInUsersTable() {
        log.debug("getNumberOfRowsInUsersTable()");
        WebElement usersTable = getWebElement(usersTableLocator);
        String xPath = ".//tbody/tr";
        List<WebElement> usersTableRows = getNestedWebElements(usersTable, By.xpath(xPath));
        return usersTableRows.size();
    }

    public boolean isUserPresentInUsersTable(String sUsername) {
        log.debug("isUserPresentInUsersTable(" + sUsername + ")");
        WebElement usersTable = getWebElement(usersTableLocator);
        String xPath = createXpathForUsernameInUsersTable(sUsername);
        return isNestedWebElementDisplayed(usersTable, By.xpath(xPath));
    }

    public String getDisplayNameInUsersTable(String sUsername) {
        log.debug("getDisplayNameInUsersTable(" + sUsername + ")");
        Assert.assertTrue(isUserPresentInUsersTable(sUsername), "User '" + sUsername + "' is NOT present in Users Table!");
        WebElement usersTable = getWebElement(usersTableLocator);
        String xPath = createXpathForDisplayNameInUsersTable(sUsername);
        WebElement displayName = getNestedWebElement(usersTable, By.xpath(xPath));
        return getTextFromWebElement(displayName);
    }

    private WebElement getHeroCountLinkWebElementInUsersTable(String sUsername) {
        Assert.assertTrue(isUserPresentInUsersTable(sUsername), "User '" + sUsername + "' is NOT present in Users Table!");
        WebElement usersTable = getWebElement(usersTableLocator);
        String xPath = createXpathForHeroCountInUsersTable(sUsername);
        return getNestedWebElement(usersTable, By.xpath(xPath));
    }

    public int getHeroCountInUsersTable(String sUsername) {
        log.debug("getHeroCountInUsersTable(" + sUsername + ")");
        WebElement heroCountLink = getHeroCountLinkWebElementInUsersTable(sUsername);
        return Integer.parseInt(getTextFromWebElement(heroCountLink));
    }

    public UserHeroesDialogBox clickHeroCountLinkInUsersTable(String sUsername) {
        log.debug("clickHeroCountLinkInUsersTable(" + sUsername + ")");
        WebElement heroCountLink = getHeroCountLinkWebElementInUsersTable(sUsername);
        clickOnWebElement(heroCountLink);
        UserHeroesDialogBox userHeroesDialogBox = new UserHeroesDialogBox(driver);
        return userHeroesDialogBox.verifyUserHeroesDialogBox();
    }

    public boolean isUserDetailsIconPresentInUsersTable(String sUsername) {
        log.debug("isUserDetailsIconPresentInUsersTable(" + sUsername + ")");
        WebElement usersTable = getWebElement(usersTableLocator);
        String xPath = createXpathForUserDetailsIconInUsersTable(sUsername);
        return isNestedWebElementDisplayed(usersTable, By.xpath(xPath));
    }

    public UserDetailsDialogBox clickUserDetailsIconInUsersTable(String sUsername) {
        log.debug("clickUserDetailsIconInUsersTable(" + sUsername + ")");
        Assert.assertTrue(isUserDetailsIconPresentInUsersTable(sUsername), "'User Details' Icon is NOT present in Users Table for User '" + sUsername + "'!");
        WebElement usersTable = getWebElement(usersTableLocator);
        String xPath = createXpathForUserDetailsIconInUsersTable(sUsername);
        WebElement userDetailsIcon = getNestedWebElement(usersTable, By.xpath(xPath));
        clickOnWebElement(userDetailsIcon);
        UserDetailsDialogBox userDetailsDialogBox = new UserDetailsDialogBox(driver);
        return userDetailsDialogBox.verifyUserDetailsDialogBox();
    }

    public boolean isEditUserIconPresentInUsersTable(String sUsername) {
        log.debug("isEditUserIconPresentInUsersTable(" + sUsername + ")");
        WebElement usersTable = getWebElement(usersTableLocator);
        Assert.assertTrue(isUserPresentInUsersTable(sUsername), "User '" + sUsername + "' is NOT present in Users Table!");
        String xPath = createXpathForEditUserIconInUsersTable(sUsername);
        return isNestedWebElementDisplayed(usersTable, By.xpath(xPath));
    }

    public EditUserDialogBox clickEditUserIconInUsersTable(String sUsername) {
        log.debug("clickEditUserIconInUsersTable(" + sUsername + ")");
        Assert.assertTrue(isEditUserIconPresentInUsersTable(sUsername), "'Edit User' Icon is NOT present in Users Table for User '" + sUsername + "'!");
        WebElement usersTable = getWebElement(usersTableLocator);
        String xPath = createXpathForEditUserIconInUsersTable(sUsername);
        WebElement editUserIcon = getNestedWebElement(usersTable, By.xpath(xPath));
        clickOnWebElement(editUserIcon);
        EditUserDialogBox editUserDialogBox = new EditUserDialogBox(driver);
        return editUserDialogBox.verifyEditUserDialogBox();
    }

    public boolean isDeleteUserIconPresentInUsersTable(String sUsername) {
        log.debug("isDeleteUserIconPresentInUsersTable(" + sUsername + ")");
        WebElement usersTable = getWebElement(usersTableLocator);
        Assert.assertTrue(isUserPresentInUsersTable(sUsername), "User '" + sUsername + "' is NOT present in Users Table!");
        String xPath = createXpathForDeleteUserIconInUsersTable(sUsername);
        return isNestedWebElementDisplayed(usersTable, By.xpath(xPath));
    }

    public DeleteUserDialogBox clickDeleteUserIconInUsersTable(String sUsername) {
        log.debug("clickDeleteUserIconInUsersTable(" + sUsername + ")");
        Assert.assertTrue(isDeleteUserIconPresentInUsersTable(sUsername), "'Delete User' Icon is NOT present in Users Table for User '" + sUsername + "'!");
        WebElement usersTable = getWebElement(usersTableLocator);
        String xPath = createXpathForDeleteUserIconInUsersTable(sUsername);
        WebElement deleteUserIcon = getNestedWebElement(usersTable, By.xpath(xPath));
        clickOnWebElement(deleteUserIcon);
        DeleteUserDialogBox deleteUserDialogBox = new DeleteUserDialogBox(driver);
        return deleteUserDialogBox.verifyDeleteUserDialogBox();
    }

}
