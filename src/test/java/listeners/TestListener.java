package listeners;

import annotations.Jira;
import org.openqa.selenium.WebDriver;
import org.testng.*;
import utils.DateTimeUtils;
import utils.LoggerUtils;
import utils.PropertiesUtils;
import utils.ScreenShotUtils;

import java.util.Arrays;

public class TestListener implements ITestListener, ISuiteListener {

    private static boolean bListenerTakeScreenShot = PropertiesUtils.getTakeScreenshots();
    private static boolean bUpdateJira = false;

    @Override
    public void onStart(ISuite suite) {
        String sSuiteName = suite.getName();
        LoggerUtils.log.info(("[SUITE STARTED] " + sSuiteName));
    }

    @Override
    public void onFinish(ISuite suite) {
        String sSuiteName = suite.getName();
        LoggerUtils.log.info(("[SUITE FINISHED] " + sSuiteName));
    }

    @Override
    public void onStart(ITestContext context) {
        String sTestGroupName = context.getName();
        bListenerTakeScreenShot = getTakeScreenShot(context);
        bUpdateJira = getUpdateJira(context);
        context.setAttribute("listenerTakeScreenShot", bListenerTakeScreenShot);
        LoggerUtils.log.info(("[TESTS STARTED] " + sTestGroupName));

        // Get parameters from suite/properties/command line... etc...

    }

    @Override
    public void onFinish(ITestContext context) {
        String sTestGroupName = context.getName();
        LoggerUtils.log.info(("[TESTS FINISHED] " + sTestGroupName));

    }

    @Override
    public void onTestStart(ITestResult result) {
        // Test name at the Class level (if exists) or test method name
        //String sTestName1 = result.getName();
        // Test name at the Class level (if exists) or NULL (or Test name at the Method level if @Test annotation doesn't exist at the class level)
        //String sTestName2 = result.getTestName();
        // Test Class Name (full path) - for One Test Case -> One Class -> One Method approach
        //String sTestName3 = result.getTestClass().getName();
        // Test name at the Class level (if exists) or Test name at the Method level (if exists) or NULL
        //String sTestName4 = result.getTestClass().getTestName();
        // Test method name (without path)
        //String sTestName5 = result.getMethod().getMethodName();
        // Test method name (full path) - for unit and integration test (multiple test methods inside one test class)
        //String sTestName6 = result.getMethod().getQualifiedName();
        // Test Class Name (without path)
        //String sTestName7 = result.getTestClass().getRealClass().getSimpleName();

        String sTestName = result.getTestClass().getName();
        LoggerUtils.log.info(("[TEST STARTED] " + sTestName));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        LoggerUtils.log.info(("[TEST SUCCESS] " + sTestName));

        // Add info to report
        // Create PASS result in Test Management Tool (Jira) - add info about environment, browser, etc...

        if(bUpdateJira) {
            Jira jira = getJiraDetails(result);
            if(jira == null) {
                LoggerUtils.log.warn("Listener cannot get Jira Details for test '" + sTestName + "!");
            } else {
                String sJiraID = jira.jiraID();
                String sTestOwner = jira.owner();
                String sTestAuthor = jira.author();
                LoggerUtils.log.info("JIRA ID: " + sJiraID);
                LoggerUtils.log.info("OWNER: " + sTestOwner);
                LoggerUtils.log.info("AUTHOR: " + sTestAuthor);

                String sBrowser = PropertiesUtils.getBrowser();
                String sEnvironment = PropertiesUtils.getEnvironment();
            }
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        LoggerUtils.log.info(("[TEST FAILED] " + sTestName));

        // Add info to report
        // Create FAIL result in Test Management Tool (Jira) - add info about environment, browser, etc...
        // Open TA bug in Bug Management Tool (Jira) - add error message, add stack trace, attach screenshot and assign to test owner
        // Open Ticket in Task Management Tool (Jira) and assign to test owner

        if(bListenerTakeScreenShot) {
            WebDriver[] drivers = getWebDriverInstances(result);
            if(drivers != null) {
                for(int i = 0; i < drivers.length; i++) {
                    String sScreenShotName = sTestName + "_" + DateTimeUtils.getDateTimeStamp();
                    if(drivers.length > 1) {
                        sScreenShotName = sScreenShotName + "_" + (i+1);
                    }
                    ScreenShotUtils.takeScreenShot(drivers[i], sScreenShotName);
                }
            } else {
                LoggerUtils.log.warn("Driver instance(s) for test '" + sTestName + "' is already null!");
            }
        }

        if(bUpdateJira) {
            Jira jira = getJiraDetails(result);
            if(jira == null) {
                LoggerUtils.log.warn("Listener cannot get Jira Details for test '" + sTestName + "!");
            } else {
                String sJiraID = jira.jiraID();
                String sTestOwner = jira.owner();
                String sTestAuthor = jira.author();
                LoggerUtils.log.info("JIRA ID: " + sJiraID);
                LoggerUtils.log.info("OWNER: " + sTestOwner);
                LoggerUtils.log.info("AUTHOR: " + sTestAuthor);

                String sBrowser = PropertiesUtils.getBrowser();
                String sEnvironment = PropertiesUtils.getEnvironment();

                String sErrorMessage = result.getThrowable().getMessage();
                String sStackTrace = Arrays.toString(result.getThrowable().getStackTrace());

                String sSubject = "Test '" + sTestName + "' failed: " + sErrorMessage;

                LoggerUtils.log.info("SUBJECT: " + sSubject);
                LoggerUtils.log.info("DESCRIPTION: " + sStackTrace);
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        LoggerUtils.log.info(("[TEST SKIPPED" + sTestName));

        // Add info to report
        // Create SKIP result in Test Management Tool (Jira) - add info about environment, browser, etc...

        if(bUpdateJira) {
            Jira jira = getJiraDetails(result);
            if(jira == null) {
                LoggerUtils.log.warn("Listener cannot get Jira Details for test '" + sTestName + "!");
            } else {
                String sJiraID = jira.jiraID();
                String sTestOwner = jira.owner();
                String sTestAuthor = jira.author();
                LoggerUtils.log.info("JIRA ID: " + sJiraID);
                LoggerUtils.log.info("OWNER: " + sTestOwner);
                LoggerUtils.log.info("AUTHOR: " + sTestAuthor);

                String sBrowser = PropertiesUtils.getBrowser();
                String sEnvironment = PropertiesUtils.getEnvironment();
            }
        }
    }

    /*
    private static WebDriver getWebDriverInstance(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        WebDriver driver = null;
        try {
            driver = (WebDriver) result.getTestClass().getRealClass().getDeclaredField("driver").get(result.getInstance());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            LoggerUtils.log.warn("Cannot get Driver instance for test '" + sTestName + "! Message: " + e.getMessage());
        }
        return driver;
    }
    */

    /*
    private static WebDriver getWebDriverInstance(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        try {
            WebDriver driver = (WebDriver) result.getTestContext().getAttribute(sTestName + ".driver");
            if(driver == null) {
                LoggerUtils.log.warn("Cannot get Driver instance for test '" + sTestName + "!");
            }
            return driver;
        } catch (Exception e) {
            LoggerUtils.log.warn("Cannot get Driver instance for test '" + sTestName + "! Message: " + e.getMessage());
        }
        return null;
    }
     */

    private static WebDriver[] getWebDriverInstances(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        try {
            WebDriver[] drivers = (WebDriver[]) result.getTestContext().getAttribute(sTestName + ".drivers");
            if(drivers.length == 0) {
                LoggerUtils.log.warn("Cannot get Driver instance(s) for test '" + sTestName + "!");
            }
            return drivers;

        } catch (Exception e) {
            LoggerUtils.log.warn("Cannot get Driver instance(s) for test '" + sTestName + "! Message: " + e.getMessage());
        }
        return null;
    }

    /*
    private static String getJiraID(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        String sJiraID = null;
        try {
            sJiraID = (String) result.getTestClass().getRealClass().getDeclaredField("sJiraID").get(result.getInstance());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            LoggerUtils.log.warn("Cannot get JiraID for test '" + sTestName + "! Message: " + e.getMessage());
        }
        return sJiraID;
    }
     */

    private static Jira getJiraDetails(ITestResult result) {
        return result.getTestClass().getRealClass().getAnnotation(Jira.class);
    }

    private static boolean getTakeScreenShot(ITestContext context) {
        String sSuiteName = context.getSuite().getName();

        // Parameter at Suite level
        String sTakeScreenShot = context.getSuite().getParameter("takeScreenShot");

        // Parameter at Test Group level
        //String sTakeScreenShot1 = context.getCurrentXmlTest().getParameter("takeScreenShot");

        if(sTakeScreenShot == null) {
            LoggerUtils.log.warn("Parameter 'takeScreenShot' is not set in '" + sSuiteName + "' suite!");
            return PropertiesUtils.getTakeScreenshots();
        }
        sTakeScreenShot = sTakeScreenShot.toLowerCase();
        if (!(sTakeScreenShot.equals("true") || sTakeScreenShot.equals("false"))) {
            LoggerUtils.log.warn("Cannot convert 'takeScreenShot' parameter value '" + sTakeScreenShot + "' to boolean value! Threat this as False!");
        }
        return Boolean.parseBoolean(sTakeScreenShot);
    }

    private static boolean getUpdateJira(ITestContext context) {
        String sSuiteName = context.getSuite().getName();
        String sUpdateJira = context.getSuite().getParameter("updateJira");
        if(sUpdateJira == null) {
            LoggerUtils.log.warn("Parameter 'updateJira' is not set in '" + sSuiteName + "' suite!");
            return false;
        }
        sUpdateJira = sUpdateJira.toLowerCase();
        if (!(sUpdateJira.equals("true") || sUpdateJira.equals("false"))) {
            LoggerUtils.log.warn("Cannot convert 'sUpdateJira' parameter value '" + sUpdateJira + "' to boolean value! Threat this as False!");
        }
        boolean bUpdateJira = Boolean.parseBoolean(sUpdateJira);
        LoggerUtils.log.info("Update Jira: " + bUpdateJira);
        return bUpdateJira;
    }

    private static String getTestDescription(ITestResult result) {
        return result.getMethod().getDescription();
    }
}
