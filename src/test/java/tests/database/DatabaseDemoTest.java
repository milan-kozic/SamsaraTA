package tests.database;

import annotations.Jira;
import data.ApiCalls;
import data.CommonStrings;
import data.Groups;
import objects.ApiError;
import objects.DatabaseHero;
import objects.DatabaseUser;
import objects.User;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import tests.BaseTestClass;
import utils.DatabaseUtils;
import utils.DateTimeUtils;
import utils.RestApiUtils;

import java.util.Date;
import java.util.List;

@Test(groups = {Groups.DATABASE})
public class DatabaseDemoTest extends BaseTestClass {

    private final String sTestName = this.getClass().getName();

    @BeforeMethod
    public void setUpTest(ITestContext testContext) {
        log.debug("[SETUP TEST] " + sTestName);

    }

    @Test
    public void testDatabaseDemoTest() {

        log.debug("[START TEST] " + sTestName);

        String sUserID = DatabaseUtils.getUserID("finn");
        log.info("UserID: " + sUserID);

        List<String> usernameList = DatabaseUtils.getAllUsernames();
        log.info(usernameList);

        DatabaseUser databaseUser = DatabaseUtils.getDatabaseUser("finn");
        log.info(databaseUser);

        List<DatabaseHero> databaseHeroList = DatabaseUtils.getDatabaseHeroesForUser(sUserID);
        log.info(databaseHeroList);

        User user = DatabaseUtils.getUser("dedoje");
        log.info(user);

    }
}
