package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JavaScriptUtils extends LoggerUtils {

    private static final String sDragAndDropJavaScriptFilePath = "javascript/drag_and_drop_simulator.js";
    //private static final String sDragAndDropJavaScriptFilePath = "javascript/drag_and_drop_helper.js";

    private static String loadJavaScriptFile(String sFilePath) {
        InputStream inputStream = JavaScriptUtils.class.getClassLoader().getResourceAsStream(sFilePath);
        StringBuilder sJavaScript = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while((line = reader.readLine()) != null) {
                sJavaScript.append(line).append(" ");
            }
        } catch (IOException e) {
            Assert.fail("Cannot read JavaScript file '" + sFilePath + "'. Message: " + e.getMessage());
        }
        return sJavaScript.toString();
    }

    public static void simulateDragAndDrop(WebDriver driver, String sourceLocator, String destinationLocator) {
        log.trace("simulateDragAndDrop()");
        String sJavaScript = loadJavaScriptFile(sDragAndDropJavaScriptFilePath);
        sJavaScript = sJavaScript + "DndSimulator.simulate('"+ sourceLocator + "', '" + destinationLocator + "');";
        //sJavaScript = sJavaScript + "$('" + sourceLocator + "').simulateDragDrop({ dropTarget: '" + destinationLocator + "'});";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(sJavaScript);
    }
}
