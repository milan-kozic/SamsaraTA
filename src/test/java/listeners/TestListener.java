package listeners;

import annotations.Jira;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.LoggerUtils;
import utils.PropertiesUtils;
import utils.ScreenShotUtils;

import java.util.Arrays;

public class TestListener extends LoggerUtils implements ITestListener {

    private static final boolean bListenerTakeScreenShot = PropertiesUtils.getTakeScreenshot();
    private static boolean bUpdateJira = false;

    @Override
    public void onStart(ITestContext context) {
        String sSuiteName = context.getSuite().getName();
        log.info("[SUITE STARTED] " + sSuiteName);
        bUpdateJira = getUpdateJira(context);
        context.setAttribute("listenerTakeScreenShot", bListenerTakeScreenShot);
    }

    @Override
    public void onFinish(ITestContext context) {
        String sSuiteName = context.getSuite().getName();
        log.info("[SUITE FINISHED] " + sSuiteName);

    }

    @Override
    public void onTestStart(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        log.info("[TEST STARTED] " + sTestName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        log.info("[TEST SUCCESS] " + sTestName);
        if(bUpdateJira) {
            Jira jira = getJiraDetails(result);
            if (jira == null) {
                log.warn("Listener cannot get Jira Details for test '" + sTestName + "'!");
            } else {
                String sJiraID = jira.jiraID();
                String owner = jira.owner();
                log.info("JiraID: " + sJiraID);
                log.info("Owner: " + owner);
                // Create PASSED result on Jira
            }
        }

    }

    @Override
    public void onTestFailure(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        log.info("[TEST FAILED] " + sTestName);
        if(bListenerTakeScreenShot) {
            WebDriver[] drivers = getWebDriverInstances(result);
            log.info("Taking ScreenShot from Listener!");
            if(drivers != null) {
                for(int i=0; i < drivers.length; i++) {
                    String sScreenShotName = sTestName;
                    if(drivers.length > 1) {
                        sScreenShotName = sScreenShotName + "_" + (i + 1);
                    }
                    String pathToScreenShot = ScreenShotUtils.takeScreenShot(drivers[i], sScreenShotName);
                }
            }
        }
        // Create FAILED result on Jira and Open Ticket with Bug
        Jira jira = getJiraDetails(result);
        String sErrorMessage = result.getThrowable().getMessage();
        String sStackTrace = Arrays.toString(result.getThrowable().getStackTrace());

    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        log.info("[TEST SKIPPED] " + sTestName);
        // delete screenshot from temp folder (if it was saved in temp folder)

    }

    private static WebDriver getWebDriverInstance(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        WebDriver driver = null;
        try {
            //driver = (WebDriver) result.getTestClass().getRealClass().getDeclaredField("driver").get(result.getInstance());
            driver = (WebDriver) result.getTestContext().getAttribute(sTestName + ".driver");
        } catch (Exception e) {
            log.warn("Cannot get Driver Instance for test '" + sTestName + "'! Message: " + e.getMessage());
        }
        return driver;
    }

    private static WebDriver[] getWebDriverInstances(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        WebDriver[] drivers = null;
        try {
            //driver = (WebDriver) result.getTestClass().getRealClass().getDeclaredField("driver").get(result.getInstance());
            drivers = (WebDriver[]) result.getTestContext().getAttribute(sTestName + ".drivers");
        } catch (Exception e) {
            log.warn("Cannot get Driver Instance(s) for test '" + sTestName + "'! Message: " + e.getMessage());
        }
        return drivers;
    }

    private static String getJiraID(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        String jiraID = null;
        try {
            jiraID = (String) result.getTestClass().getRealClass().getDeclaredField("sJiraID").get(result.getInstance());
            log.info("JiraID: " + jiraID);
            jiraID = (String) result.getTestContext().getAttribute(sTestName + ".jiraID");
            log.info("JiraID: " + jiraID);
            jiraID = result.getTestName();
            log.info("JiraID: " + jiraID);
            jiraID = result.getMethod().getDescription();
            log.info("JiraID: " + jiraID);
            jiraID = Arrays.toString(result.getMethod().getGroups());
            log.info("JiraID: " + jiraID);
        } catch (Exception e) {
            log.warn("Cannot get JiraID for test '" + sTestName + "'! Message: " + e.getMessage());
        }
        return jiraID;
    }

    private static Jira getJiraDetails(ITestResult result) {
        return result.getTestClass().getRealClass().getAnnotation(Jira.class);
    }

    private static boolean getUpdateJira(ITestContext context) {
        String sSuiteName = context.getSuite().getName();
        String sUpdateJira = context.getSuite().getParameter("updateJira");
        //String sUpdateJira = context.getCurrentXmlTest().getParameter("updateJira");

        if (sUpdateJira == null) {
            log.warn("Parameter 'updateJira' is not set in '" + sSuiteName + "' suite!");
            return false;
        } else {
            sUpdateJira = sUpdateJira.toLowerCase();
            if (!(sUpdateJira.equals("true") || sUpdateJira.equals("false"))) {
                log.warn("Parameter 'updateJira' in '" + sSuiteName + "' suite is not recognized as boolean value!to boolean value!");
                return false;
            }
        }
        boolean bUpdateJira = Boolean.parseBoolean(sUpdateJira);
        log.info("Update Jira: " + bUpdateJira);
        return bUpdateJira;
    }

}
