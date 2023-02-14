package tests.heroes;

import annotations.Jira;
import data.CommonStrings;
import data.Groups;
import data.Time;
import objects.Hero;
import objects.User;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.*;
import tests.BaseTestClass;
import utils.DateTimeUtils;
import utils.RestApiUtils;

import java.util.Date;

@Jira(jiraID = "JIRA00010", owner = "Heroes Team")
@Test(groups = {Groups.REGRESSION, Groups.SANITY, Groups.HEROES})
public class DeleteHero extends BaseTestClass {

    private final String sTestName = this.getClass().getName();
    private WebDriver driver;

    private User user;
    private Hero hero;
    private boolean bCreated = false;


    @BeforeMethod
    public void setUpTest(ITestContext testContext) {
        log.debug("[SETUP TEST] " + sTestName);

        driver = setUpDriver();
        testContext.setAttribute(sTestName + ".drivers", new WebDriver[]{driver});

        user = User.createNewUniqueUser("DeleteHero");
        RestApiUtils.postUser(user);
        bCreated = true;
        user.setCreatedAt(RestApiUtils.getUser(user.getUsername()).getCreatedAt());
        log.info("User: " + user);

        hero = Hero.createNewUniqueHero(user, "DeletedHero");
        RestApiUtils.postHero(hero);
        hero.setCreatedAt(RestApiUtils.getHero(hero.getHeroName()).getCreatedAt());
        log.info("Hero: " + hero);
    }

    @Test
    public void testDeleteHero() {

        String sExpectedDeleteHeroMessage = CommonStrings.getDeleteHeroMessage(hero.getHeroName(), hero.getHeroClass(), hero.getHeroLevel());
        log.info("Expected Delete Hero Message: " + sExpectedDeleteHeroMessage);

        log.debug("[START TEST] " + sTestName);

        LoginPage loginPage = new LoginPage(driver).open();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        WelcomePage welcomePage = loginPage.login(user);
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        HeroesPage heroesPage = welcomePage.clickHeroesTab();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        heroesPage = heroesPage.search(hero.getHeroName());
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        DeleteHeroDialogBox deleteHeroDialogBox = heroesPage.clickDeleteHeroIconInHeroesTable(hero.getHeroName());
        String sActualDeleteHeroMessage = deleteHeroDialogBox.getDeleteHeroMessage();
        Assert.assertEquals(sActualDeleteHeroMessage, sExpectedDeleteHeroMessage, "Wrong Delete Hero Message!");

        heroesPage = deleteHeroDialogBox.clickDeleteButton();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        Assert.assertFalse(RestApiUtils.checkIfHeroExists(hero.getHeroName()), "Hero '" + hero.getHeroName() + "' is NOT deleted!");

        User savedUser = RestApiUtils.getUser(user.getUsername());
        int numberOfHeroes = savedUser.getHeroCount();
        Assert.assertEquals(numberOfHeroes, 0, "User's Hero is NOT deleted!");

    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest(ITestResult testResult) {
        log.debug("[END TEST] " + sTestName);
        tearDown(driver, testResult);
        if(bCreated) {
            cleanUp();
        }
    }

    private void cleanUp() {
        log.debug("cleanUp()");
        try {
            RestApiUtils.deleteUser(user.getUsername());
        } catch (AssertionError | Exception e) {
            log.error("Exception occurred in cleanUp(" + sTestName + ")! Message: " + e.getMessage());
        }
    }
}
