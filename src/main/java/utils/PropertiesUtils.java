package utils;

import org.testng.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils extends LoggerUtils {

    private static final String sPropertiesFilePath = "common.properties";

    private static final Properties properties = loadPropertiesFile();

    public static Properties loadPropertiesFile(String sFilePath) {
        InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(sFilePath);
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            Assert.fail("Cannot load " + sFilePath + " file! Message: " + e.getMessage());
        }
        return properties;
    }

    private static Properties loadPropertiesFile() {
        return loadPropertiesFile(sPropertiesFilePath);
    }

    private static String getProperty(String sProperty) {
        log.trace("getProperty(" + sProperty + ")");
        String result = properties.getProperty(sProperty);
        Assert.assertNotNull(result, "Cannot find property '" + sProperty + "' in " + sPropertiesFilePath + " file!");
        return result;
    }

    public static String getBrowser() {
        return getProperty("browser");
    }

    public static String getEnvironment() {
        return getProperty("environment");
    }

    public static boolean getRemote() {
        String sRemote = getProperty("remote").toLowerCase();
        if (!(sRemote.equals("true") || sRemote.equals("false"))) {
            Assert.fail("Cannot convert 'Remote' property value '" + sRemote + "' to boolean value!");
        }
        return Boolean.parseBoolean(sRemote);
    }

    public static boolean getHeadless() {
        String sHeadless = getProperty("headless").toLowerCase();
        if (!(sHeadless.equals("true") || sHeadless.equals("false"))) {
            Assert.fail("Cannot convert 'Headless' property value '" + sHeadless + "' to boolean value!");
        }
        return Boolean.parseBoolean(sHeadless);
    }

    public static String getLocale() {
        return getProperty("locale");
    }

    public static boolean getTakeScreenshot() {
        String sTakeScreenshot = getProperty("takeScreenshot").toLowerCase();
        if (!(sTakeScreenshot.equals("true") || sTakeScreenshot.equals("false"))) {
            Assert.fail("Cannot convert 'TakeScreenshot' property value '" + sTakeScreenshot + "' to boolean value!");
        }
        return Boolean.parseBoolean(sTakeScreenshot);
    }

    public static String getHubUrl() {
        return getProperty("hubUrl");
    }

    public static String getDriversFolder() {
        return getProperty("driversFolder");
    }

    public static String getScreenshotsFolder() {
        return getProperty("screenshotsFolder");
    }

    public static String getReportsFolder() {
        return getProperty("reportsFolder");
    }

    public static String getImagesFolder() {
        return getProperty("imagesFolder");
    }

    public static String getDocumentsFolder() {
        return getProperty("documentsFolder");
    }

    public static String getLocalBaseUrl() {
        return getProperty("localBaseUrl");
    }

    public static String getTestBaseUrl() {
        return getProperty("testBaseUrl");
    }

    public static String getProdBaseUrl() {
        return getProperty("prodBaseUrl");
    }

    public static String getBaseUrl() {
        String sEnvironment = getEnvironment().toLowerCase();
        String sBaseUrl = null;
        switch (sEnvironment) {
            case "local" : {
                sBaseUrl = getLocalBaseUrl();
                break;
            }
            case "test" : {
                sBaseUrl = getTestBaseUrl();
                break;
            }
            case "prod" : {
                sBaseUrl = getProdBaseUrl();
                break;
            }
            default : {
                Assert.fail("Cannot get BaseUrl! Environment '" + sEnvironment + "' is not recognized!");
            }
        }
        return sBaseUrl;
    }

    public static String getAdminUsername() {
        return getProperty("adminUsername");
    }

    public static String getAdminPassword() {
        return getProperty("adminPassword");
    }

    public static String getRootUsername() {
        return getProperty("rootUsername");
    }

    public static String getRootPassword() {
        return getProperty("rootPassword");
    }

    public static String getEndUserUsername() {
        return getProperty("endUserUsername");
    }

    public static String getEndUserPassword() {
        return getProperty("endUserPassword");
    }

    public static String getDefaultPassword() {
        return getProperty("defaultPassword");
    }

    public static String getLocalDataSourceUrl() {
        return getProperty("localDataSourceUrl");
    }

    public static String getTestDataSourceUrl() {
        return getProperty("testDataSourceUrl");
    }

    public static String getProdDataSourceUrl() {
        return getProperty("prodDataSourceUrl");
    }

    public static String getDataSourceUrl() {
        String sEnvironment = getEnvironment().toLowerCase();
        String sDataSourceUrl = null;
        switch (sEnvironment) {
            case "local" : {
                sDataSourceUrl = getLocalDataSourceUrl();
                break;
            }
            case "test" : {
                sDataSourceUrl = getTestDataSourceUrl();
                break;
            }
            case "prod" : {
                sDataSourceUrl = getProdDataSourceUrl();
                break;
            }
            default : {
                Assert.fail("Cannot get DataSourceUrl! Environment '" + sEnvironment + "' is not recognized!");
            }
        }
        return sDataSourceUrl;
    }

}
