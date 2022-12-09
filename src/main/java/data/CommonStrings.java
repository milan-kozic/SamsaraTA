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

}
