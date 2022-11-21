package utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

public class ScreenShotUtils extends LoggerUtils {

    private static final String sScreenShotFolder = System.getProperty("user.dir") + PropertiesUtils.getScreenshotsFolder();

    private static String createScreenShotPath(String sTestName) {
        return sScreenShotFolder + sTestName + ".png";
    }

    public static String takeScreenShot(WebDriver driver, String sTestName) {
        log.trace("takeScreenShot(" + sTestName + ")");
        if (WebDriverUtils.hasDriverQuit(driver)) {
            log.warn("Screenshot for test '" + sTestName + "' could not be taken! Driver instance has quit!");
            return null;
        }
        //String sessionID = WebDriverUtils.getSessionID(driver).toString();
        //String sScreenShotName = sTestName + "_" + sessionID;
        String pathToFile = createScreenShotPath(sTestName);
        TakesScreenshot screenshot = ((TakesScreenshot) driver);
        File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
        File dstFile = new File(pathToFile);
        try {
            FileUtils.copyFile(srcFile, dstFile);
            log.info("Screenshot for test '" + sTestName + "' is saved in file: " + pathToFile);
        } catch (IOException e) {
            log.warn("Screenshot for test '" + sTestName + "' could not be saved in file '" + pathToFile + "'. Message: " + e.getMessage());
            return null;
        }
        return pathToFile;
    }
}
