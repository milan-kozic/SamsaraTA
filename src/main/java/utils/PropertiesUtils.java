package utils;

import org.testng.Assert;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

    private static final String sPropertiesPath = "common.properties";

    private static final Properties properties = loadPropertiesFile();

    public static Properties loadPropertiesFile(String sFilePath) {
        Properties properties = new Properties();
        InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(sFilePath);
        try {
            //sFilePath = System.getProperty("user.dir") + "/src/main/resources/common.properties";
            //FileReader reader = new FileReader(sFilePath);
            //properties.load(reader);
            properties.load(inputStream);
        } catch (Exception e) {
            Assert.fail("Cannot load " + sFilePath + " file! Message: " + e.getMessage());
        }
        return properties;
    }

    public static Properties loadPropertiesFile() {
        return loadPropertiesFile(sPropertiesPath);
    }

    private static String getProperty(String sProperty) {
        String sResult = properties.getProperty(sProperty);
        Assert.assertNotNull(sResult, "Cannot find property '" + sProperty + "' in " + sPropertiesPath + " file!");
        return sResult;
    }

    public static String getBrowser() {
        String sBrowser = System.getProperty("browser");
        if(sBrowser == null) {
            sBrowser = getProperty("browser").toLowerCase();
        }
        return sBrowser.toLowerCase();
    }

    public static String getEnvironment() {
        String sEnvironment = System.getProperty("environment");
        if (sEnvironment == null) {
            sEnvironment = getProperty("environment");
        }
        return sEnvironment;
    }

    public static String getLocale() {
        String sLocale = System.getProperty("locale");
        if (sLocale == null) {
            sLocale = getProperty("locale");
        }
        return sLocale;
    }

    private static String getLocalBaseUrl() {
        return getProperty("localBaseUrl");
    }

    private static String getTestBaseUrl() {
        return getProperty("testBaseUrl");
    }

    private static String getStageBaseUrl() {
        return getProperty("stageBaseUrl");
    }

    private static String getProdBaseUrl() {
        return getProperty("prodBaseUrl");
    }

    public static String getBaseUrl() {
        String sEnvironment = getEnvironment().toLowerCase();
        return getBaseUrl(sEnvironment);
    }

    public static String getBaseUrl(String sEnvironment) {
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
            case "stage" : {
                sBaseUrl = getStageBaseUrl();
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

    public static boolean getRemote() {
        String sRemote = System.getProperty("remote");
        if (sRemote == null) {
            sRemote = getProperty("remote");
        }
        sRemote = sRemote.toLowerCase();
        if (!(sRemote.equals("true") || sRemote.equals("false"))) {
            Assert.fail("Cannot convert 'Remote' property value '" + sRemote + "' to boolean value!");
        }
        return Boolean.parseBoolean(sRemote);
    }

    public static boolean getHeadless() {
        String sHeadless = System.getProperty("headless");
        if (sHeadless == null) {
            sHeadless = getProperty("headless");
        }
        sHeadless = sHeadless.toLowerCase();
        if (!(sHeadless.equals("true") || sHeadless.equals("false"))) {
            Assert.fail("Cannot convert 'Headless' property value '" + sHeadless + "' to boolean value!");
        }
        return Boolean.parseBoolean(sHeadless);
    }

    public static String getUsername() {
        return getProperty("username");
    }

    public static String getPassword() {
        return getProperty("password");
    }

    public static String getDefaultPassword() {
        return getProperty("defaultPassword");
    }

    public static String getAdminUsername() {
        return getProperty("adminUsername");
    }

    public static String getAdminPassword() {
        return getProperty("adminPassword");
    }

    public static String getAdminGmailAccount() {
        return getProperty("adminGmailAccount");
    }

    public static String getAdminGmailPassword() {
        return getProperty("adminGmailPassword");
    }

    public static String getRootUsername() {
        return getProperty("rootUsername");
    }

    public static String getRootPassword() {
        return getProperty("rootPassword");
    }

    public static String getDriversFolder() {
        return getProperty("driversFolder");
    }

    public static String getFilesFolder() {
        return getProperty("filesFolder");
    }

    public static String getDefaultSecretQuestion() {
        return getProperty("defaultSecretQuestion");
    }

    public static String getDefaultSecretAnswer() {
        return getProperty("defaultSecretAnswer");
    }

    public static String getScreenshotsFolder() {
        return getProperty("screenshotsFolder");
    }

    public static String getImagesFolder() {
        return getProperty("imagesFolder");
    }

    public static String getHubUrl() {
        return getProperty("hubUrl");
    }

    public static boolean getTakeScreenshots() {
        String sTakeScreenshots = getProperty("takeScreenshots");
        sTakeScreenshots = sTakeScreenshots.toLowerCase();
        if (!(sTakeScreenshots.equals("true") || sTakeScreenshots.equals("false"))) {
            Assert.fail("Cannot convert 'TakeScreenshots' property value '" + sTakeScreenshots + "' to boolean value!");
        }
        return Boolean.parseBoolean(sTakeScreenshots);
    }

    private static String getLocalDataSourceUrl() {
        return getProperty("localDataSourceUrl");
    }

    private static String getTestDataSourceUrl() {
        return getProperty("testDataSourceUrl");
    }

    private static String getStageDataSourceUrl() {
        return getProperty("stageDataSourceUrl");
    }

    private static String getProdDataSourceUrl() {
        return getProperty("prodDataSourceUrl");
    }

    public static String getDataSourceUrl() {
        String sEnvironment = getEnvironment().toLowerCase();
        return getDataSourceUrl(sEnvironment);
    }

    public static String getDataSourceUrl(String sEnvironment) {
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
            case "stage" : {
                sDataSourceUrl = getStageDataSourceUrl();
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

    public static String getDatabaseDriver() {
        return getProperty("databaseDriver");
    }

    public static String getExtentReportsFolder() {
        return getProperty("extentReportsFolder");
    }

}
