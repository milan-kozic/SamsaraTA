package utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.testng.Assert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScreenShotUtils {

    private static final String screenShotPath = System.getProperty("user.dir") + PropertiesUtils.getScreenshotsFolder();
    private static final String imagesPath = System.getProperty("user.dir") + PropertiesUtils.getImagesFolder();

    private static String createScreenShotPath(String sTestName) {
        return screenShotPath + sTestName + ".png";
    }


    private static String createActualSnapShotPath(String imageName, String sDateTimeStamp) {
        return screenShotPath + imageName + "_" + sDateTimeStamp + "_Actual.png";
    }
    private static String createExpectedSnapShotPath(String imageName, String sDateTimeStamp) {
        return screenShotPath + imageName + "_" + sDateTimeStamp + "_Expected.png";
    }
    private static String createDifferenceSnapShotPath(String imageName, String sDateTimeStamp) {
        return screenShotPath + imageName + "_" + sDateTimeStamp + "_Difference.png";
    }

    private static String removeImageExtension(String imageFile) {
        return imageFile.split("\\.")[0];
    }

    public static String takeScreenShot(WebDriver driver, String sTestName) {
        LoggerUtils.log.trace("takeScreenShot(" + sTestName + ")");

        String sFilePath = createScreenShotPath(sTestName);

        if(WebDriverUtils.hasDriverQuit(driver)) {
            LoggerUtils.log.warn("ScreenShot for test '" + sTestName + "' could not be taken! Driver instance has already quit!");
            return null;
        }

        TakesScreenshot screenshot = (TakesScreenshot) driver;
        File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
        File dstFile = new File(sFilePath);
        try {
            FileUtils.copyFile(srcFile, dstFile);
            LoggerUtils.log.info("ScreenShot for test '" + sTestName + "' is saved: " + sFilePath);
        } catch (IOException exc) {
            LoggerUtils.log.warn("ScreenShot for test '" + sTestName + "' could not be saved in file '" + sFilePath + "'. Message: " + exc.getMessage());
            return null;
        }
        return sFilePath;
    }

    public static BufferedImage takeScreenShot(WebDriver driver) {
        LoggerUtils.log.trace("takeScreenShot()");
        if(WebDriverUtils.hasDriverQuit(driver)) {
            Assert.fail("ScreenShot could not be taken! Driver instance has already quit!");
        }
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        BufferedImage fullScreen = null;
        try {
            fullScreen = ImageIO.read(screenshot.getScreenshotAs(OutputType.FILE));
        } catch (IOException e) {
            Assert.fail("ScreenShot could not be taken! Message: " + e.getMessage());
        }
        return fullScreen;
    }

    public static BufferedImage takeSnapShotOfWebElement(WebDriver driver, WebElement element) {
        LoggerUtils.log.trace("takeSnapShotOfWebElement(" + element + ")");
        if(WebDriverUtils.hasDriverQuit(driver)) {
            Assert.fail("SnapShot of Web Element '" + element + "' could not be taken! Driver instance has already quit!");
        }
        BufferedImage fullScreen = takeScreenShot(driver);
        Point elementLocation = element.getLocation();
        Dimension elementDimension = element.getSize();
        return fullScreen.getSubimage(elementLocation.getX(), elementLocation.getY(), elementDimension.getWidth(), elementDimension.getHeight());
    }

    public static BufferedImage takeSnapShotOfWebElement(WebElement element) {
        LoggerUtils.log.trace("takeSnapShotOfWebElement(" + element + ")");
        BufferedImage snapshotOfWebElement = null;
        try {
            snapshotOfWebElement = ImageIO.read(element.getScreenshotAs(OutputType.FILE));
        } catch (IOException e) {
            Assert.fail("SnapShot of Web Element '" + element + "' could not be taken! Message: " + e.getMessage());
        }
        return snapshotOfWebElement;
    }

    public static void saveBufferedImage(BufferedImage bufferedImage, String pathToFile) {
        LoggerUtils.log.trace("saveBufferedImage(" + pathToFile + ")");
        File file = new File(pathToFile);
        try {
            ImageIO.write(bufferedImage, "PNG", file);
        } catch (IOException e) {
            LoggerUtils.log.warn("Image could not be saved in file '" + pathToFile + "'. Message: " + e.getMessage());
        }
    }

    public static BufferedImage loadBufferedImage(String imageFile) {
        LoggerUtils.log.trace("loadBufferedImage(" + imageFile + ")");
        String sFilePath = imagesPath + imageFile;
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(sFilePath));
        } catch (IOException e) {
            Assert.fail("Cannot load image from file '" + sFilePath + "'. Message: " + e.getMessage());
        }
        return image;
    }

    public static boolean compareSnapShotWithImage(BufferedImage actualImage, String imageFile, int threshold, int pixelDiff, boolean makeDiff) {
        LoggerUtils.log.trace("compareSnapShotWithImage(" + imageFile + ")");
        BufferedImage expectedImage = loadBufferedImage(imageFile);
        if(actualImage.getWidth() != expectedImage.getWidth() || actualImage.getHeight() != expectedImage.getHeight()) {
            LoggerUtils.log.warn("Snapshot has different dimensions from image '" + imageFile + "! Snapshot: " + actualImage.getWidth() + "x" + actualImage.getHeight() + "pix. Image: " + expectedImage.getWidth() + "x" + expectedImage.getHeight() + "pix.");
            return false;
        }
        int width = actualImage.getWidth();
        int height = actualImage.getHeight();

        boolean bDiffPixel;
        boolean bEqual = true;

        int iNumberOfDiffPixels = 0;

        BufferedImage diffImage = new BufferedImage(width, height, actualImage.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(threshold == 0) {
                    bDiffPixel = actualImage.getRGB(x, y) != expectedImage.getRGB(x, y);
                } else {
                    bDiffPixel = 100 * compareARGB(actualImage.getRGB(x, y), expectedImage.getRGB(x, y)) > threshold;
                }

                if(bDiffPixel) {
                    iNumberOfDiffPixels++;
                    if (makeDiff) {
                       diffImage.setRGB(x, y, 0xFFFF0000);
                       if (iNumberOfDiffPixels > pixelDiff) {
                           bEqual = false;
                       }
                    } else {
                        if (iNumberOfDiffPixels > pixelDiff) {
                            return false;
                        }
                    }
                } else {
                    if(makeDiff) {
                        diffImage.setRGB(x, y, actualImage.getRGB(x, y));
                    }
                }
            }
        }

        if(makeDiff && !bEqual) {
            LoggerUtils.log.warn("Snapshot is not equal to image '" + imageFile + "'! Difference size: " + iNumberOfDiffPixels);
            String sImageName = removeImageExtension(imageFile);
            String sDateTimeStamp = DateTimeUtils.getDateTimeStamp();
            String pathToActualImage = createActualSnapShotPath(sImageName, sDateTimeStamp);
            String pathToExpectedImage = createExpectedSnapShotPath(sImageName, sDateTimeStamp);
            String pathToDifferenceImage = createDifferenceSnapShotPath(sImageName, sDateTimeStamp);
            saveBufferedImage(actualImage, pathToActualImage);
            saveBufferedImage(expectedImage, pathToExpectedImage);
            saveBufferedImage(diffImage, pathToDifferenceImage);
            LoggerUtils.log.info("Actual Image: " + pathToActualImage);
            LoggerUtils.log.info("Expected Image: " + pathToExpectedImage);
            LoggerUtils.log.info("Difference Image: " + pathToDifferenceImage);
        }
        return bEqual;
    }

    public static Point getImageCenterLocation(WebDriver driver, String imageFile, int threshold, int pixelDiff) {
        LoggerUtils.log.trace("getImageCenterLocation(" + imageFile + ")");

        BufferedImage fullImage = takeScreenShot(driver);
        BufferedImage subImage = loadBufferedImage(imageFile);
        Point location = findSubImage(fullImage, subImage, threshold, pixelDiff);
        Assert.assertNotNull(location, "Image '" + imageFile + "' is NOT present on screen!");
        int xOffset = subImage.getWidth() / 2;
        int yOffset = subImage.getHeight() / 2;
        return new Point (location.getX() + xOffset, location.getY() + yOffset);
    }

    private static Point findSubImage(BufferedImage fullImage, BufferedImage subImage, int threshold, int pixelDiff) {
        int w1 = fullImage.getWidth();
        int h1 = fullImage.getHeight();
        int w2 = subImage.getWidth();
        int h2 = subImage.getHeight();

        if (w2 > w1 || h2 > h1) {
            return null;
        }

        for(int x = 0; x < w1 - w2; x++) {
            for (int y = 0; y < h1 - h2; y++) {
                BufferedImage tempSubImage = fullImage.getSubimage(x, y, w2, h2);
                if (compareImages(tempSubImage, subImage, threshold, pixelDiff)) {
                   return new Point(x, y);
                }
            }
        }
        return null;
    }

    private static boolean compareImages(BufferedImage imageA, BufferedImage imageB, int threshold, int pixelDif) {
        if(imageA.getWidth() != imageB.getWidth() || imageA.getHeight() != imageB.getHeight()) {
            return false;
        }
        int width = imageA.getWidth();
        int height = imageA.getHeight();
        boolean bDiffPixel;
        int iNumberOfDiffPixels = 0;
        // ARGB
        // A = 8bits = alpha (00000000 - completely transparent, 11111111 - not transparent at all)
        // R = Red shade (0...255 values)
        // G = Green shade (0...255 values)
        // B = Blue shade (0...255 values)
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                if(threshold == 0) {
                    bDiffPixel =  imageA.getRGB(x, y) != imageB.getRGB(x, y);
                } else {
                    bDiffPixel = 100 * compareARGB(imageA.getRGB(x, y), imageB.getRGB(x, y)) > threshold;
                }
                if(bDiffPixel) {
                    iNumberOfDiffPixels++;
                    if(iNumberOfDiffPixels > pixelDif) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static double compareARGB(int rgb1, int rgb2) {
        double b1 = (rgb1 & 0xFF) / 255.0;
        double b2 = (rgb2 & 0xFF) / 255.0;

        double g1 = ((rgb1 >> 8) & 0xFF) / 255.0;
        double g2 = ((rgb2 >> 8) & 0xFF) / 255.0;

        double r1 = ((rgb1 >> 16) & 0xFF) / 255.0;
        double r2 = ((rgb2 >> 16) & 0xFF) / 255.0;

        double a1 = ((rgb1 >> 24) & 0xFF) / 255.0;
        double a2 = ((rgb2 >> 24) & 0xFF) / 255.0;

        return a1 * a2 * Math.sqrt((r1-r2)*(r1-r2) + (g1-g2)*(g1-g2) + (b1-b2)*(b1-b2)) / Math.sqrt(3);
    }
}
