package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils extends LoggerUtils {

    public static void wait (int seconds) {
        try {
            Thread.sleep(1000L * seconds);
        } catch (InterruptedException e) {
            log.warn("InterruptedException in Thread.sleep(). Message: " + e.getMessage());
        }
    }

    public static Date getCurrentDateTime() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    public static Date getDateTime(long milliseconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milliseconds);
        return cal.getTime();
    }

    public static String getFormattedDateTime(Date date, String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    public static String getLocalizedDateTime(Date date, String pattern, String locale) {
        Locale loc = new Locale(locale);
        DateFormat dateFormat = new SimpleDateFormat(pattern, loc);
        return dateFormat.format(date);
    }

    private static String getBrowserDateTimeString(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String sBrowserDateTime = (String) js.executeScript("var browserDateTime = new Date(); return Intl.DateTimeFormat('en-GB', {dateStyle: 'full', timeStyle: 'long'}).format(browserDateTime);");
        return sBrowserDateTime.replace(" at ", " ");
    }

    public static Date getBrowserDateTime(WebDriver driver) {
        String sBrowserDateTime = getBrowserDateTimeString(driver);
        return getParsedDateTime(sBrowserDateTime, "EEEE, dd MMMM yyyy HH:mm:ss z");
    }

    public static String getBrowserTimeZone(WebDriver driver) {
        // return (long) js.executeScript("return new Date().getTimezoneOffset()");
        // var browserDateTime = new Date();
        // browserDate.getFullYear();
        // browserDate.getMonth();
        // browserDate.getDate();
        // browserDate.getHours();
        // browserDate.getMinutes();
        // browserDate.getSeconds();
        // browserDate.getTimezoneOffset();
        String sBrowserDateTime = getBrowserDateTimeString(driver);
        int pos = sBrowserDateTime.lastIndexOf(" ");
        return sBrowserDateTime.substring(pos + 1);
    }

    public static Date getParsedDateTime(String sDateTime, String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = dateFormat.parse(sDateTime);
        } catch (ParseException e) {
            Assert.fail("Cannot parse date '" + sDateTime + "' using pattern '" + pattern + "'! Message: " + e.getMessage());
        }
        return date;
    }

    public static String getFormattedCurrentDateTime(String pattern) {
        Date date = getCurrentDateTime();
        return getFormattedDateTime(date, pattern);
    }

    public static String getDateTimeStamp() {
        return getFormattedCurrentDateTime("yyMMddHHmmss");
    }

    public static boolean compareDateTimes(Date date1, Date date2, int threshold) {
        long diff = (date2.getTime() - date1.getTime()) / 1000;
        log.debug("Comparing dates (Date 1: " + date1 + ", Date 2: " + date2 + "). Difference: " + diff + " seconds. Threshold: " + threshold + ".");
        return Math.abs(diff) <= threshold;
    }
}
