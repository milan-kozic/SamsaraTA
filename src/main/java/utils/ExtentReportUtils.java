package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.nio.charset.StandardCharsets;

public class ExtentReportUtils extends LoggerUtils {

    private static final String sExtentReportsBaseFolder = System.getProperty("user.dir") + PropertiesUtils.getReportsFolder();

    public static String getExtentReportFolder(String sSuiteName) {
        return sExtentReportsBaseFolder + sSuiteName.replace(" ", "_").toLowerCase() + "\\";
    }

    public static String getExtentReportName(String sSuiteName) {
        return sSuiteName.replace(" ", "_").toLowerCase();
    }

    public static String getExtentReportFilesFolderName(String sSuiteName) {
        return getExtentReportName(sSuiteName) + "_files";
    }

    public static String getExtentReportFilesFolder(String sSuiteName) {
        return getExtentReportFolder(sSuiteName) + getExtentReportFilesFolderName(sSuiteName) + "\\";
    }

    public static String getExtentReportFilePath(String sSuiteName) {
        return getExtentReportFolder(sSuiteName) + getExtentReportName(sSuiteName) + ".html";
    }

    public static ExtentReports createExtentReportInstance(String sSuiteName) {
        String sExtentReportPath = getExtentReportFilePath(sSuiteName);

        ExtentSparkReporter extentReporter = new ExtentSparkReporter(sExtentReportPath);
        extentReporter.config().setEncoding(StandardCharsets.UTF_8.toString());
        extentReporter.config().setReportName(sSuiteName + " Report");
        extentReporter.config().setDocumentTitle(sSuiteName + " Results");
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
