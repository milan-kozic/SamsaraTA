package pages;

import data.PageUrlPaths;
import data.Time;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import utils.ScreenShotUtils;

public class PracticePage extends CommonLoggedInPage {

    // Page Url Path
    private final String PRACTICE_PAGE_URL = getPageUrl(PageUrlPaths.PRACTICE_PAGE);

    // Locators

    //private final String draggableImageLocatorString = "img#drag-image";
    private final String draggableImageLocatorString = "img.draggable-image";
    private final String dragAreaLocatorString = "div#drag-area";
    private final String dropAreaLocatorString = "div#drop-area";

    private final By draggableImageLocator = By.id("drag-image");

    @FindBy(xpath = "//div[@id='useless-tooltip']/p[contains(@class,'h4 heading')]")
    private WebElement uselessTooltipTitle;

    @FindBy(id = "useless-tooltip-text")
    WebElement uselessTooltip;

    @FindBy(id = "drag-area")
    private WebElement dragArea;

    @FindBy(id = "drop-area")
    private WebElement dropArea;

    @FindBy(id = "drag-image")
    private WebElement draggableImage;

    @FindBy(xpath = "//div[@id='drag-area']/img[@id='drag-image']")
    private WebElement draggableImageInDragArea;

    @FindBy(xpath = "//div[@id='drop-area']/img[@id='drag-image']")
    private WebElement draggableImageInDropArea;

    @FindBy(id="drag-and-drop-message")
    private WebElement dragAndDropMessage;

    // Constructor
    public PracticePage(WebDriver driver) {
        super(driver);
        log.trace("new PracticePage()");
    }

    public PracticePage open() {
        return open(true);
    }

    public PracticePage open(boolean bVerify) {
        log.debug("Open PracticePage (" + PRACTICE_PAGE_URL + ")");
        openUrl(PRACTICE_PAGE_URL);
        if (bVerify) {
            verifyPracticePage();
        }
        return this;
    }

    public PracticePage verifyPracticePage() {
        log.debug("verifyPracticePage()");
        waitForUrlChange(PRACTICE_PAGE_URL, Time.TIME_SHORTER);
        waitUntilPageIsReady(Time.TIME_SHORT);
        return this;
    }

    public boolean isUselessTooltipDisplayed() {
        log.debug("isUselessTooltipDisplayed()");
        return isWebElementDisplayed(uselessTooltip);
    }

    public String getUselessTooltip() {
        log.debug("getUselessTooltip()");
        moveMouseToWebElement(uselessTooltipTitle);
        Assert.assertTrue(isUselessTooltipDisplayed(), "Useless tooltip is NOT displayed on Practice Page!");
        return getTextFromWebElement(uselessTooltip);
    }

    public boolean isDragAndDropMessageDisplayed() {
        log.debug("isDragAndDropMessageDisplayed()");
        return isWebElementDisplayed(dragAndDropMessage);
    }

    public String getDragAndDropMessage() {
        log.debug("getDragAndDropMessage()");
        Assert.assertTrue(isDragAndDropMessageDisplayed(), "Drag and Drop Message is NOT displayed on Practice Page!");
        return getTextFromWebElement(dragAndDropMessage);
    }

    public boolean isImagePresentInDragArea() {
        log.debug("isImagePresentInDragArea()");
        //return isNestedWebElementDisplayed(dragArea, draggableImageLocator);
        return isWebElementDisplayed(draggableImageInDragArea);
    }

    public boolean isImagePresentInDropArea() {
        log.debug("isImagePresentInDropArea()");
        //return isNestedWebElementDisplayed(dropArea, draggableImageLocator);
        return isWebElementDisplayed(draggableImageInDropArea);
    }

    public PracticePage dragAndDropImage() {
        log.debug("dragAndDropImage()");
        doDragAndDropJS(draggableImageLocatorString, dropAreaLocatorString);
        PracticePage practicePage = new PracticePage(driver);
        return practicePage.verifyPracticePage();
    }

    public WelcomePage clickSamsaraImage() {
        log.debug("clickSamsaraImage()");
        String sSamsaraImage = "SamsaraLogo.png";
        Point location = ScreenShotUtils.getImageCenterLocation(driver, sSamsaraImage, 5);
        clickOnLocationJS(location);
        WelcomePage welcomePage = new WelcomePage(driver);
        return welcomePage.verifyWelcomePage();
    }
}
