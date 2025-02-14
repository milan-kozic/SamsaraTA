package listeners;

import annotations.Jira;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.testng.*;
import utils.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class TestListener implements ITestListener, ISuiteListener {

    private static boolean bListenerTakeScreenShot = PropertiesUtils.getTakeScreenshots();
    private static boolean bUpdateJira = false;

    private static String sExtentReportFolder;
    private static String sExtentReportName;
    private static String sExtentReportFilesFolderName;
    private static String sExtentReportFilesFolder;

    private static ExtentReports extentReport = null;
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onStart(ISuite suite) {
        String sSuiteName = suite.getName();
        LoggerUtils.log.info(("[SUITE STARTED] " + sSuiteName));

        sExtentReportFolder = ExtentReportsUtils.getExtentReportFolder(sSuiteName);
        sExtentReportName = ExtentReportsUtils.getExtentReportName(sSuiteName);
        sExtentReportFilesFolderName = ExtentReportsUtils.getExtentReportsFilesFolderName(sSuiteName);
        sExtentReportFilesFolder = ExtentReportsUtils.getExtentReportFilesFolder(sSuiteName);

        try {
            FileUtils.cleanDirectory(new File(sExtentReportFolder));
        } catch (Exception e) {
            LoggerUtils.log.warn("Extent Report Folder '" + sExtentReportFolder + "' cannot be cleaned! Message: " + e.getMessage());
        }

        extentReport = ExtentReportsUtils.createExtentReportInstance(sSuiteName);
    }

    @Override
    public void onFinish(ISuite suite) {
        String sSuiteName = suite.getName();
        LoggerUtils.log.info(("[SUITE FINISHED] " + sSuiteName));
        if (extentReport != null) {
            extentReport.flush();
        }
    }

    @Override
    public void onStart(ITestContext context) {
        String sTestGroupName = context.getName();
        bListenerTakeScreenShot = getTakeScreenShot(context);
        bUpdateJira = getUpdateJira(context);
        setBrowserProperty(context);
        context.setAttribute("listenerTakeScreenShot", bListenerTakeScreenShot);
        LoggerUtils.log.info(("[TESTS STARTED] " + sTestGroupName));
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

        ExtentTest test = extentReport.createTest(sTestName);

        // String sPackage = result.getTestClass().getRealClass().getPackage().getName();
        // test.assignCategory(sPackage);

        String[] groups = result.getMethod().getGroups();
        for(String group : groups) {
            test.assignCategory(group);
        }

        Jira jira = getJiraDetails(result);

        if (jira != null) {
            test.info("JiraID: " + jira.jiraID());
            test.assignAuthor(jira.owner());
        } else {
            test.assignAuthor("Unknown");
        }
        extentTest.set(test);
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

        String logText = createPassedTestLogText(sTestName);
        Markup markup = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
        extentTest.get().log(Status.PASS, markup);
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
                    String sRelativeScreenshotPath = takeAndCopyScreenshot(drivers[i], sScreenShotName);

                    String sSession = "";
                    if (drivers.length > 1) {
                        sSession = " (Session " + (i + 1) + ")";
                    }
                    if (sRelativeScreenshotPath != null) {
                        extentTest.get().fail("Screenshot of failure" + sSession + " (click on thumbnail to enlarge)", MediaEntityBuilder.createScreenCaptureFromPath(sRelativeScreenshotPath).build());
                    } else {
                        extentTest.get().fail("Screenshot of failure" + sSession + " could NOT be captured!");
                    }

                }
            } else {
                LoggerUtils.log.warn("Driver instance(s) for test '" + sTestName + "' is already null!");
                extentTest.get().fail("Screenshot of failure could NOT be captured!");
            }
        }

        String logText = createFailedTestLogText(sTestName);
        Markup markup = MarkupHelper.createLabel(logText, ExtentColor.RED);
        extentTest.get().log(Status.FAIL, markup);

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

        String logText = createSkippedTestLogText(sTestName);
        Markup markup = MarkupHelper.createLabel(logText, ExtentColor.ORANGE);
        extentTest.get().log(Status.SKIP, markup);

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

    private static String getBrowser(ITestContext context) {
        String sSuiteName = context.getSuite().getName();
        String sBrowser = context.getSuite().getParameter("browser");
        if(sBrowser == null) {
            LoggerUtils.log.warn("Parameter 'browser' is not set in '" + sSuiteName + "' suite!");
        }
        return sBrowser;
    }

    private static String getEnvironment(ITestContext context) {

        String sSuiteName = context.getSuite().getName();
        String sEnvironment = context.getSuite().getParameter("environment");
        //String sEnvironment = context.getCurrentXmlTest().getParameter("environment");
        if (sEnvironment == null) {
            LoggerUtils.log.warn("Parameter 'environment' is not set in '" + sSuiteName + "' suite!");

        }
        return sEnvironment;
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

    private static void setBrowserProperty(ITestContext context) {
        String sBrowser = System.getProperty("browser");
        if(sBrowser == null) {
            sBrowser = getBrowser(context);
        }
        if(sBrowser != null) {
            System.setProperty("browser", sBrowser);
        }
    }

    private static void setEnvironmentProperty(ITestContext context) {
        String sEnvironment = System.getProperty("environment");
        if (sEnvironment == null) {
            sEnvironment = getEnvironment(context);
            if (sEnvironment != null) {
                System.setProperty("environment", sEnvironment);
            }

        }
    }

    private static String createPassedTestLogText(String sTestName) {
        return "<b>Test " + sTestName + " Passed</b>";
    }

    private static String createSkippedTestLogText(String sTestName) {
        return "<b>Test " + sTestName + " Skipped</b>";
    }

    private static String createFailedTestLogText(String sTestName) {
        return "<b>Test " + sTestName + " Failed</b>";
    }

    private static String createFailedTestErrorLog(ITestResult result) {
        StackTraceElement[] stackTraceElements = result.getThrowable().getStackTrace();
        //String sExceptionStackTrace = Arrays.toString(result.getThrowable().getStackTrace());
        StringBuilder sExceptionStackTrace = new StringBuilder();
        for(StackTraceElement st : stackTraceElements) {
            sExceptionStackTrace.append(st.toString()).append("<br>");
        }
        String sExceptionMessage = result.getThrowable().getMessage();
        //sExceptionStackTrace = sExceptionStackTrace.replaceAll(",", "<br>");
        return "<font color=red><b>" + sExceptionMessage + "</b><details><summary>" + "\nClick to see details" + "</font></summary>" + sExceptionStackTrace + "</details> \n";
    }

    private static String takeAndCopyScreenshot(WebDriver driver, String sTestName) {
        String sSourcePath = ScreenShotUtils.takeScreenShot(driver, sTestName);
        if (sSourcePath == null) {
            return null;
        }
        File srcScreenShot = new File(sSourcePath);
        String sScreenshotName = srcScreenShot.getName();
        String sDestinationPath = sExtentReportFilesFolder + sScreenshotName;
        File dstScreenShot = new File(sDestinationPath);
        try {
            FileUtils.copyFile(srcScreenShot, dstScreenShot);
        } catch (IOException e) {
            LoggerUtils.log.warn("Screenshot '" + sScreenshotName + "' could not be copied in folder '" + sExtentReportFilesFolder + "'. Message: " + e.getMessage());
            return null;
        }
        return sExtentReportFilesFolderName + "/" + srcScreenShot.getName();
    }
}
