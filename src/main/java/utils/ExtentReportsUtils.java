package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.nio.charset.StandardCharsets;

public class ExtentReportsUtils extends LoggerUtils {

    private static final String extentReportsBaseFolder = System.getProperty("user.dir") + PropertiesUtils.getExtentReportsFolder();

    public static String getExtentReportFolder(String sSuiteName) {
        return extentReportsBaseFolder + sSuiteName.replace(" ", "_").toLowerCase() + "\\";
    }

    private static String getExtentReportFilePath(String sSuiteName) {
        return getExtentReportFolder(sSuiteName) + getExtentReportName(sSuiteName) + ".html";
    }

    public static String getExtentReportsFilesFolderName(String sSuiteName) {
        return getExtentReportName(sSuiteName) + "_files";
    }

    public static String getExtentReportFilesFolder(String sSuiteName) {
        return getExtentReportFolder(sSuiteName) + getExtentReportsFilesFolderName(sSuiteName) + "\\";
    }

    public static String getExtentReportName(String sSuiteName) {
        return sSuiteName.replace(" ", "_").toLowerCase();
        //return sSuiteName.toLowerCase().replace(" ", "_") + "_" + DateTimeUtils.getDateTimeStamp();
    }

    public static ExtentReports createExtentReportInstance(String sSuiteName) {
        String sExtentReportsPath = getExtentReportFilePath(sSuiteName);
        ExtentSparkReporter extentReporter = new ExtentSparkReporter(sExtentReportsPath);
        extentReporter.config().setEncoding(String.valueOf(StandardCharsets.UTF_8));
        extentReporter.config().setDocumentTitle(sSuiteName + " Report");
        extentReporter.config().setReportName(sSuiteName + " Results");
        extentReporter.config().setTheme(Theme.STANDARD);

        ExtentReports extentReport = new ExtentReports();
        extentReport.setSystemInfo("Environment", PropertiesUtils.getBaseUrl());
        extentReport.setSystemInfo("Browser", PropertiesUtils.getBrowser());
        extentReport.setSystemInfo("Headless", String.valueOf(PropertiesUtils.getHeadless()));
        extentReport.setSystemInfo("Remote", String.valueOf(PropertiesUtils.getRemote()));
        extentReport.attachReporter(extentReporter);
        return extentReport;
    }

}
