package utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.testng.Assert;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScreenShotUtils extends LoggerUtils {

    private static final String sScreenShotFolder = System.getProperty("user.dir") + PropertiesUtils.getScreenshotsFolder();
    private static final String sImageFolder = System.getProperty("user.dir") + PropertiesUtils.getImagesFolder();

    private static String createScreenShotPath(String sTestName) {
        return sScreenShotFolder + sTestName + "_" + DateTimeUtils.getDateTimeStamp() + ".png";
    }

    private static String createWebElementSnapShotPath(String sElementName) {
        return sScreenShotFolder + sElementName + ".png";
    }

    private static String createActualSnapShotPath(String sImageName, String sDateTimeStamp) {
        return sScreenShotFolder + sImageName + "_" + sDateTimeStamp + "_Actual.png";
    }

    private static String createExpectedSnapShotPath(String sImageName, String sDateTimeStamp) {
        return sScreenShotFolder + sImageName + "_" + sDateTimeStamp + "_Expected.png";
    }

    private static String createDifferenceSnapShotPath(String sImageName, String sDateTimeStamp) {
        return sScreenShotFolder + sImageName + "_" + sDateTimeStamp + "_Difference.png";
    }

    public static String takeScreenShot(WebDriver driver, String sTestName) {
        log.trace("takeScreenShot(" + sTestName + ")");
        if (WebDriverUtils.hasDriverQuit(driver)) {
            log.warn("Screenshot for test '" + sTestName + "' could not be taken! Driver instance has quit!");
            return null;
        }
        // String sessionID = WebDriverUtils.getSessionID(driver).toString();
        // String sScreenShotName = sTestName + "_" + sessionID;
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

    public static BufferedImage takeScreenShot(WebDriver driver) {
        log.trace("takeScreenShot()");
        if (WebDriverUtils.hasDriverQuit(driver)) {
            Assert.fail("Screenshot could not be taken! Driver instance has quit!");
        }
        TakesScreenshot screenshot = ((TakesScreenshot) driver);
        BufferedImage fullScreen = null;
        try {
            fullScreen = ImageIO.read(screenshot.getScreenshotAs(OutputType.FILE));
        } catch (IOException e) {
            Assert.fail("Screenshot could not be taken! Message: " + e.getMessage());
        }
        return fullScreen;
    }

    public static Screenshot takeScreenShotAS(WebDriver driver) {
        log.trace("takeScreenShotAS()");
        AShot shot = new AShot();
        return shot.takeScreenshot(driver);
    }

    public static BufferedImage takeSnapShotOfWebElement(WebDriver driver, WebElement element) {
        log.trace("takeSnapShotOfWebElement(" + element + ")");
        if (WebDriverUtils.hasDriverQuit(driver)) {
            Assert.fail("SnapShot of web element + '" + element + "' could not be taken! Driver instance has quit!");
        }
        BufferedImage fullScreen = takeScreenShot(driver);
        Point elementLocation = element.getLocation();
        Dimension elementDimension = element.getSize();
        return fullScreen.getSubimage(elementLocation.getX(), elementLocation.getY(), elementDimension.getWidth(), elementDimension.getHeight());
    }

    public static Screenshot takeSnapShotOfWebElementAS(WebDriver driver, WebElement element) {
        log.trace("takeSnapShotOfWebElementAS(" + element + ")");
        AShot shot = new AShot();
        return shot.takeScreenshot(driver, element);
    }

    private static void saveBufferedImage(BufferedImage bufferedImage, String pathToFile) {
        log.trace("saveBufferedImage(" + pathToFile + ")");
        File file = new File(pathToFile);
        try {
            ImageIO.write(bufferedImage, "PNG", file);
        } catch (IOException e) {
            log.warn("Image could not be saved in file '" + file + "'. Message: " + e.getMessage());
        }
    }

    public static void saveSnapShotOfWebElement(WebDriver driver, WebElement element, String sFileName) {
        log.trace("saveSnapShotOfWebElement(" + element + ", " + sFileName + ")");
        String sPathToFile = createWebElementSnapShotPath(sFileName);
        BufferedImage bufferedImage = takeSnapShotOfWebElement(driver, element);
        saveBufferedImage(bufferedImage, sPathToFile);
        log.info("SnapShot of WebElement is saved: " + sPathToFile);
    }

    public static void saveSnapShotOfWebElementAS(WebDriver driver, WebElement element, String sFileName) {
        log.trace("saveSnapShotOfWebElementAS(" + element + ", " + sFileName + ")");
        String sPathToFile = createWebElementSnapShotPath(sFileName);
        Screenshot screenshot = takeSnapShotOfWebElementAS(driver, element);
        BufferedImage bufferedImage = screenshot.getImage();
        saveBufferedImage(bufferedImage, sPathToFile);
        log.info("SnapShot of WebElement is saved: " + sPathToFile);
    }

    private static BufferedImage loadBufferedImage(String sFileName) {
        log.trace("loadBufferedImage(" + sFileName + ")");
        String sImagePath = sImageFolder + sFileName;
        BufferedImage bufferedImage = null;
        File file = new File(sImagePath);
        try {
            bufferedImage = ImageIO.read(file);
        } catch (IOException e) {
            Assert.fail("Cannot load image from file '" + sImagePath + "'. Message: " + e.getMessage());
        }
        return bufferedImage;
    }

    public static boolean compareSnapShotWithImage(BufferedImage actualImage, String sFileName, int threshold, int diffTrigger) {
        log.trace("compareSnapShotWithImage(" + sFileName + ")");
        BufferedImage expectedImage = loadBufferedImage(sFileName);
        if (actualImage.getWidth() != expectedImage.getWidth() || actualImage.getHeight() != expectedImage.getHeight()) {
            return false;
        }
        if (threshold < 0 || threshold > 100) {
            Assert.fail("Threshold value must be between 0 and 100. Actual value: " + threshold);
        }
        int iNumberOfDiffPixels = 0;
        int w = actualImage.getWidth();
        int h = actualImage.getHeight();

        BufferedImage differenceImage = new BufferedImage(w, h, expectedImage.getType());

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                boolean bDiffPixel;
                if (threshold == 0) {
                    bDiffPixel = actualImage.getRGB(x, y) != expectedImage.getRGB(x, y);
                } else {
                    bDiffPixel = (100 * compareARGB(actualImage.getRGB(x, y), expectedImage.getRGB(x, y))) > threshold;
                }
                if (bDiffPixel) {
                    iNumberOfDiffPixels++;
                    differenceImage.setRGB(x, y, 0xFF00FF00);
                } else {
                    differenceImage.setRGB(x, y, actualImage.getRGB(x, y));
                }
            }
        }

        if (iNumberOfDiffPixels > diffTrigger) {
            log.warn("Snapshot is not equal to image '" + sFileName + "'! Difference Size: " + iNumberOfDiffPixels);
            String sImageName = sFileName.substring(0, sFileName.lastIndexOf('.'));
            saveDiffImages(sImageName, actualImage, expectedImage, differenceImage);
            return false;
        }
        return true;
    }


    public static boolean compareSnapShotWithImageAS(Screenshot snapShot, String sFileName, int threshold, int diffTrigger) {
        log.trace("compareSnapShotWithImageAS(" + sFileName + ")");
        BufferedImage expectedImage = loadBufferedImage(sFileName);
        BufferedImage actualImage = snapShot.getImage();
        ImageDiffer imgDiff = new ImageDiffer();
        ImageDiff diff = imgDiff.withColorDistortion(threshold).makeDiff(expectedImage, actualImage);
        boolean bDiff = diff.withDiffSizeTrigger(diffTrigger).hasDiff();
        if(bDiff) {
            log.warn("Snapshot is not equal to image '" + sFileName + "'! Difference Size: " + diff.getDiffSize());
            BufferedImage differenceImage = diff.getMarkedImage();
            String sImageName = sFileName.substring(0, sFileName.lastIndexOf('.'));
            saveDiffImages(sImageName, actualImage, expectedImage, differenceImage);
        }
        return !bDiff;
    }

    private static void saveDiffImages(String sImageName, BufferedImage actualImage, BufferedImage expectedImage, BufferedImage differenceImage) {
        String sDateTimeStamp = DateTimeUtils.getDateTimeStamp();
        String pathToActualImage = createActualSnapShotPath(sImageName, sDateTimeStamp);
        String pathToExpectedImage = createExpectedSnapShotPath(sImageName, sDateTimeStamp);
        String pathToDifferenceImage = createDifferenceSnapShotPath(sImageName, sDateTimeStamp);
        saveBufferedImage(actualImage, pathToActualImage);
        saveBufferedImage(expectedImage, pathToExpectedImage);
        saveBufferedImage(differenceImage, pathToDifferenceImage);
        log.info("Actual Image: " + pathToActualImage);
        log.info("Expected Image: " + pathToExpectedImage);
        log.info("Difference Image: " + pathToDifferenceImage);
    }

    private static boolean compareImages(BufferedImage imageA, BufferedImage imageB, int threshold) {
        if (imageA.getWidth() != imageB.getWidth() || imageA.getHeight() != imageB.getHeight()) {
            return false;
        }
        int w = imageA.getWidth();
        int h = imageA.getHeight();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                boolean bDiffPixel;
                if (threshold == 0) {
                    bDiffPixel = imageA.getRGB(x, y) != imageB.getRGB(x, y);
                } else {
                    bDiffPixel = (100 * compareARGB(imageA.getRGB(x, y), imageB.getRGB(x, y))) > threshold;
                }
                if (bDiffPixel) {
                    return false;
                }
            }
        }
        return true;
    }

    private static Point findSubImage(BufferedImage fullImage, BufferedImage subImage, int threshold) {
        int w1 = fullImage.getWidth();
        int h1 = fullImage.getHeight();
        int w2 = subImage.getWidth();
        int h2 = subImage.getHeight();

        if (w2 > w1 || h2 > h1) {
            return null;
        }

        for (int x = 0; x < w1 - w2; x++) {
            for (int y = 0; y < h1 - h2; y++) {
                if (compareImages(fullImage.getSubimage(x, y, w2, h2), subImage, threshold)) {
                    return new Point(x, y);
                }
            }
        }
        return null;
    }

    public static Point getImageLocation(WebDriver driver, String sImageFile, int threshold) {
        log.trace("getImageLocation(" + sImageFile + ")");
        BufferedImage fullImage = takeScreenShot(driver);
        BufferedImage subImage = loadBufferedImage(sImageFile);
        Point location = findSubImage(fullImage, subImage, threshold);
        Assert.assertNotNull(location, "Image '" + sImageFile + "' is NOT found on the screen!");
        return location;
    }

    public static Point getImageCenterLocation(WebDriver driver, String sImageFile, int threshold) {
        log.trace("getImageCenterLocation(" + sImageFile + ")");
        BufferedImage fullImage = takeScreenShot(driver);
        BufferedImage subImage = loadBufferedImage(sImageFile);
        Point location = findSubImage(fullImage, subImage, threshold);
        Assert.assertNotNull(location, "Image '" + sImageFile + "' is NOT found on the screen!");
        int xOffset = subImage.getWidth() / 2;
        int yOffset = subImage.getHeight() / 2;
        return new Point (location.getX() + xOffset, location.getY() + yOffset);
    }

    private static double compareARGB(int rgb1, int rgb2) {
        double b1 = (rgb1 & 0x000000FF) / 255.0;
        double b2 = (rgb2 & 0x000000FF) / 255.0;
        double g1 = ((rgb1 >> 8) & 0x000000FF) / 255.0;
        double g2 = ((rgb2 >> 8) & 0x000000FF) / 255.0;
        double r1 = ((rgb1 >> 16) & 0x000000FF) / 255.0;
        double r2 = ((rgb2 >> 16) & 0x000000FF) / 255.0;
        double a1 = ((rgb1 >> 24) & 0x000000FF) / 255.0;
        double a2 = ((rgb2 >> 24) & 0x000000FF) / 255.0;
        return a1 * a2 * Math.sqrt((r1-r2)*(r1-r2) + (g1-g2)*(g1-g2) + (b1-b2)*(b1-b2)) / Math.sqrt(3);
    }
}
