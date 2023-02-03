package data;

import org.testng.Assert;
import utils.PropertiesUtils;

import java.util.Properties;

public final class CommonStrings {

    private static final String sLocaleFile = "locale_" + PropertiesUtils.getLocale() + ".loc";
    private static final String sLocaleFolder = "\\locale\\";
    private static final String sLocalePath = sLocaleFolder + sLocaleFile;

    private static final Properties locale = PropertiesUtils.loadPropertiesFile(sLocalePath);

    private static String getLocaleString(String sTitle) {
        String result = locale.getProperty(sTitle);
        Assert.assertNotNull(result, "String '" + sTitle + "' doesn't exist in file " + sLocaleFile + "!");
        return result;
    }

    // Login Page
    public static String getLogoutSuccessMessage() {
        return getLocaleString("LOGOUT_SUCCESS_MESSAGE");
    }
    public static String getLoginErrorMessage() {
        return getLocaleString("LOGIN_ERROR_MESSAGE");
    }
    public static String getRegisterSuccessMessage() {
        return getLocaleString("REGISTER_SUCCESS_MESSAGE");
    }

    // DeleteHero DialogBox
    public static String getDeleteHeroMessage(String sHeroName, String sHeroClass, Integer iHeroLevel) {
        return getLocaleString("DELETE_HERO_MESSAGE").replace("%HERO_NAME%", sHeroName).replace("%HERO_CLASS%", sHeroClass).replace("%HERO_LEVEL%", String.valueOf(iHeroLevel));
    }

    // Practice Page
    public static String getUselessTooltipText() {
        return getLocaleString("USELESS_TOOLTIP_TEXT");
    }
    public static String getDragAndDropMessage() {
        return getLocaleString("DRAG_AND_DROP_MESSAGE");
    }

    // API Errors
    public static String getApiErrorInternalServerError() {
        return getLocaleString("API_ERROR_INTERNAL_SERVER_ERROR");
    }
    public static String getApiErrorForbidden() {
        return getLocaleString("API_ERROR_FORBIDDEN");
    }

    // API Exceptions
    public static String getApiExceptionIllegalArgumentException() {
        return getLocaleString("API_EXCEPTION_ILLEGAL_ARGUMENT_EXCEPTION");
    }

    // API Messages
    public static String getApiMessageNonExistingUser(String sUsername) {
        return getLocaleString("API_MESSAGE_NON_EXISTING_USER").replace("%USERNAME%", sUsername);
    }
    public static String getApiMessageAlreadyExistingUser(String sUsername) {
        return getLocaleString("API_MESSAGE_ALREADY_EXISTING_USER").replace("%USERNAME%", sUsername);
    }
    public static String getApiMessageAccessDenied() {
        return getLocaleString("API_MESSAGE_ACCESS_DENIED");
    }
    public static String getApiMessageEmailNotSpecified() {
        return getLocaleString("API_MESSAGE_EMAIL_NOT_SPECIFIED");
    }

}
