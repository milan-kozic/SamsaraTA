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
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ExtentReportUtils;
import utils.LoggerUtils;
import utils.PropertiesUtils;
import utils.ScreenShotUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class TestListener extends LoggerUtils implements ITestListener {

    private static final boolean bListenerTakeScreenShot = PropertiesUtils.getTakeScreenshot();
    private static boolean bUpdateJira = false;

    private static String sExtentReportFolder;
    private static String sExtentReportName;
    private static String sExtentReportFilesFolderName;
    private static String sExtentReportFilesFolder;
    private static String sExtentReportFilePath;

    private static ExtentReports extentReport = null;

    private static final ThreadLocal<ExtentTest> extentTestThread = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        String sSuiteName = context.getSuite().getName();
        log.info("[SUITE STARTED] " + sSuiteName);
        bUpdateJira = getUpdateJira(context);
        context.setAttribute("listenerTakeScreenShot", bListenerTakeScreenShot);

        sExtentReportFolder = ExtentReportUtils.getExtentReportFolder(sSuiteName);
        sExtentReportName = ExtentReportUtils.getExtentReportName(sSuiteName);
        sExtentReportFilesFolderName = ExtentReportUtils.getExtentReportFilesFolderName(sSuiteName);
        sExtentReportFilesFolder = ExtentReportUtils.getExtentReportFilesFolder(sSuiteName);
        sExtentReportFilePath = ExtentReportUtils.getExtentReportFilePath(sSuiteName);

        try {
            FileUtils.deleteDirectory(new File(sExtentReportFolder));
        } catch (Exception e) {
            log.warn("Extent Report Folder '" + sExtentReportFolder + "' cannot be cleaned! Message: " + e.getMessage());
        }

        extentReport = ExtentReportUtils.createExtentReportInstance(sSuiteName);
    }

    @Override
    public void onFinish(ITestContext context) {
        String sSuiteName = context.getSuite().getName();
        log.info("[SUITE FINISHED] " + sSuiteName);
        if (extentReport != null) {
            extentReport.flush();
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        log.info("[TEST STARTED] " + sTestName);

        ExtentTest test = extentReport.createTest(sTestName);

        Jira jira = getJiraDetails(result);
        if (jira != null) {
            test.info("JiraID: " + jira.jiraID());
            test.assignAuthor(jira.owner());
        }

//        String sPackage = result.getTestClass().getRealClass().getPackage().getName();
//        test.assignCategory(sPackage);

        String[] groups = result.getMethod().getGroups();
        for(String group : groups) {
            test.assignCategory(group);
        }
        extentTestThread.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        log.info("[TEST SUCCESS] " + sTestName);

        // Create PASSED result on Jira
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

            String sText = "<b>Test " + sTestName + " Passed!</b>";
            Markup markup = MarkupHelper.createLabel(sText, ExtentColor.GREEN);
            extentTestThread.get().log(Status.PASS, markup);
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        log.info("[TEST FAILED] " + sTestName);

        String sErrorLog = createFailedTestErrorLog(result);
        extentTestThread.get().fail(sErrorLog);

        if(bListenerTakeScreenShot) {
            WebDriver[] drivers = getWebDriverInstances(result);
            log.info("Taking ScreenShot from Listener!");
            if(drivers != null) {
                for(int i=0; i < drivers.length; i++) {
                    String sScreenShotName = sTestName;
                    String sSession = "";
                    if(drivers.length > 1) {
                        sScreenShotName = sScreenShotName + "_" + (i + 1);
                        sSession = " (Session " + (i + 1) + ")";
                    }
                    String sRelativeScreenShotPath = takeAndCopyScreenShot(drivers[i], sScreenShotName);
                    if(sRelativeScreenShotPath != null) {
                        extentTestThread.get().fail("Screenshot of failure" + sSession + " (click on thumbnail to enlarge)", MediaEntityBuilder.createScreenCaptureFromPath(sRelativeScreenShotPath).build());
                    } else {
                        extentTestThread.get().fail("Screenshot of failure" + sSession + " could NOT be captured!");
                    }
                }
            }
        }
        // Create FAILED result on Jira and Open Ticket with Bug
        if(bUpdateJira) {
            Jira jira = getJiraDetails(result);
            if (jira == null) {
                log.warn("Listener cannot get Jira Details for test '" + sTestName + "'!");
            } else {
                String sJiraID = jira.jiraID();
                String owner = jira.owner();
                log.info("JiraID: " + sJiraID);
                log.info("Owner: " + owner);
                String sErrorMessage = result.getThrowable().getMessage();
                String sStackTrace = Arrays.toString(result.getThrowable().getStackTrace());
            }
        }

        // Add ScreenShot(s)
        // Add Error Message
        // Add StackTrace

        String sText = "<b>Test " + sTestName + " Failed!</b>";
        Markup markup = MarkupHelper.createLabel(sText, ExtentColor.RED);
        extentTestThread.get().log(Status.FAIL, markup);

    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        log.info("[TEST SKIPPED] " + sTestName);
        // delete screenshot from temp folder (if it was saved in temp folder)

        String sText = "<b>Test " + sTestName + " Skipped!</b>";
        Markup markup = MarkupHelper.createLabel(sText, ExtentColor.ORANGE);
        extentTestThread.get().log(Status.SKIP, markup);
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

    private static String createFailedTestErrorLog(ITestResult result) {
        String sMessage = result.getThrowable().getMessage();
        StackTraceElement[] stackTraceElements = result.getThrowable().getStackTrace();
        StringBuilder sStackTrace = new StringBuilder();
        for(StackTraceElement st : stackTraceElements) {
            sStackTrace.append(st.toString()).append("<br>");
        }
        return "<font color=red><b>" + sMessage + "</b>" + "<details><summary>" + "\nClick to see details" + "</font></summary>" + sStackTrace + "</details> \n";
    }

    private static String takeAndCopyScreenShot(WebDriver driver, String sTestName) {
        String sSourcePath = ScreenShotUtils.takeScreenShot(driver, sTestName);
        if (sSourcePath == null) {
            return null;
        }

        File srcScreenShot = new File(sSourcePath);
        String sScreenShotName = srcScreenShot.getName();
        String sDestinationPath = sExtentReportFilesFolder + sScreenShotName;
        File dstScreenShot = new File(sDestinationPath);
        try {
            FileUtils.copyFile(srcScreenShot, dstScreenShot);
        } catch (IOException e) {
            log.warn("Screenshot '" + sScreenShotName + "' could not be copied in folder '" + sExtentReportFilesFolder + "'. Message: " + e.getMessage());
            return null;
        }
        return sExtentReportFilesFolderName + "/" + sScreenShotName;
    }

}
