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

    public static String getHubUrl() {
        return getProperty("hubUrl");
    }

    public static String getDriversFolder() {
        return getProperty("driversFolder");
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


}
