package utils;

import com.google.gson.Gson;
import data.ApiCalls;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import objects.Hero;
import objects.User;
import org.testng.Assert;
import pages.BasePageClass;

public class RestApiUtils extends LoggerUtils {

    private static final String BASE_URL = BasePageClass.getBaseUrl();
    private static final String ADMIN_USERNAME = PropertiesUtils.getAdminUsername();
    private static final String ADMIN_PASSWORD = PropertiesUtils.getAdminPassword();


    private static Response checkIfUserExistsApiCall(String sUsername, String sAuthUser, String sAuthPass) {
        String sApiCall = BASE_URL + ApiCalls.createCheckIfUserExistsApiCall(sUsername);
        Response response = null;
        try {
            response = RestAssured.given().auth().basic(sAuthUser, sAuthPass)
                    .headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                    .when().get(sApiCall);
        } catch (Exception e) {
            Assert.fail("Exception in checkIfUserExists(" + sUsername + ") Api Call: " + e.getMessage());
        }
        return response;
    }

    public static boolean checkIfUserExists(String sUsername, String sAuthUser, String sAuthPass) {
        log.debug("checkIfUserExists(" + sUsername + ")");
        Response response = checkIfUserExistsApiCall(sUsername, sAuthUser, sAuthPass);
        int status = response.getStatusCode();
        String sResponseBody = response.getBody().asString();
        Assert.assertEquals(status, 200, "Wrong Response Status Code in checkIfUserExists(" + sUsername + ") Api Call! Response Body: " + sResponseBody);
        if (!(sResponseBody.equalsIgnoreCase("true") || sResponseBody.equalsIgnoreCase("false"))) {
            Assert.fail("Cannot convert response body '" + sResponseBody + "' to boolean value!");
        }
        return Boolean.parseBoolean(sResponseBody);
    }

    public static boolean checkIfUserExists(String sUsername) {
        return checkIfUserExists(sUsername, ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    private static Response getUserApiCall(String sUsername, String sAuthUser, String sAuthPass) {
        String sApiCall = BASE_URL + ApiCalls.createGetUserApiCall(sUsername);
        Response response = null;
        try {
            response = RestAssured.given().auth().basic(sAuthUser, sAuthPass)
                    .headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                    .when().get(sApiCall);
        } catch (Exception e) {
            Assert.fail("Exception in getUser(" + sUsername + ") Api Call: " + e.getMessage());
        }
        return response;
    }

    public static String getUserJsonFormat(String sUsername, String sAuthUser, String sAuthPass) {
        log.debug("getUserJsonFormat(" + sUsername + ")");
        Assert.assertTrue(checkIfUserExists(sUsername, sAuthUser, sAuthPass), "User '" + sUsername + "' doesn't exist!");
        Response response = getUserApiCall(sUsername, sAuthUser, sAuthPass);
        int status = response.getStatusCode();
        String sResponseBody = response.getBody().asPrettyString();
        Assert.assertEquals(status, 200, "Wrong Response Status Code in getUser(" + sUsername + ") Api Call! Response Body: " + sResponseBody);
        return sResponseBody;
    }

    public static String getUserJsonFormat(String sUsername) {
        return getUserJsonFormat(sUsername, ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    public static User getUser(String sUsername, String sAuthUser, String sAuthPass) {
        log.debug("getUser(" + sUsername + ")");
        String json = getUserJsonFormat(sUsername, sAuthUser, sAuthPass);
        Gson gson = new Gson();
        //Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.fromJson(json, User.class);
    }

    public static User getUser(String sUsername) {
        return getUser(sUsername, ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    private static Response postUserApiCall(User user, String sAuthUser, String sAuthPass) {
        String sApiCall = BASE_URL + ApiCalls.createPostUserApiCall();
        Response response = null;
        try {
            Gson gson = new Gson();
            String json = gson.toJson(user, User.class);
            response = RestAssured.given().auth().basic(sAuthUser, sAuthPass)
                    .headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                    .body(json)
                    .when().redirects().follow(false)
                    .post(sApiCall);
        } catch (Exception e) {
            Assert.fail("Exception in postUser(" + user.getUsername() + ") Api Call: " + e.getMessage());
        }
        return response;
    }

    public static void postUser(User user, String sAuthUser, String sAuthPass) {
        log.debug("postUser(" + user.getUsername() + ")");
        Assert.assertFalse(checkIfUserExists(user.getUsername(), sAuthUser, sAuthPass), "User '" + user.getUsername() + " already exists!");
        Response response = postUserApiCall(user, sAuthUser, sAuthPass);
        int status = response.getStatusCode();
        String sResponseBody = response.getBody().asString();
        Assert.assertEquals(status, 200, "Wrong Response Status Code in postUser(" + user.getUsername() + ") Api Call! Response Body: " + sResponseBody);
        log.debug("User Created: " + checkIfUserExists(user.getUsername(), sAuthUser, sAuthPass));
    }

    public static void postUser(User user) {
        postUser(user, ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    private static Response deleteUserApiCall(String sUsername, String sAuthUser, String sAuthPass) {
        String sApiCall = BASE_URL + ApiCalls.createDeleteUserApiCall(sUsername);
        Response response = null;
        try {
            response = RestAssured.given().auth().basic(sAuthUser, sAuthPass)
                    .headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                    .when().redirects().follow(false)
                    .delete(sApiCall);
        } catch (Exception e) {
            Assert.fail("Exception in deleteUser(" + sUsername + ") Api Call: " + e.getMessage());
        }
        return response;
    }

    public static void deleteUser(String sUsername, String sAuthUser, String sAuthPass) {
        log.debug("deleteUser(" + sUsername + ")");
        Assert.assertTrue(checkIfUserExists(sUsername, sAuthUser, sAuthPass), "User '" + sUsername + " doesn't exist!");
        Response response = deleteUserApiCall(sUsername, sAuthUser, sAuthPass);
        int status = response.getStatusCode();
        String sResponseBody = response.getBody().asString();
        Assert.assertEquals(status, 200, "Wrong Response Status Code in deleteUser(" + sUsername + ") Api Call! Response Body: " + sResponseBody);
        log.debug("User Deleted: " + !checkIfUserExists(sUsername, sAuthUser, sAuthPass));
    }

    public static void deleteUser(String sUsername) {
        deleteUser(sUsername, ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    private static Response checkIfHeroExistsApiCall(String sHeroName, String sAuthUser, String sAuthPass) {
        String sApiCall = BASE_URL + ApiCalls.createCheckIfHeroExistsApiCall(sHeroName);
        Response response = null;
        try {
            response = RestAssured.given().auth().basic(sAuthUser, sAuthPass)
                    .headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                    .get(sApiCall);
        } catch (Exception e) {
            Assert.fail("Exception in checkIfHeroExists(" + sHeroName + ") Api Call: " + e.getMessage());
        }
        return response;
    }

    public static boolean checkIfHeroExists(String sHeroName, String sAuthUser, String sAuthPass) {
        log.trace("checkIfHeroExists(" + sHeroName + ")");
        Response response = checkIfHeroExistsApiCall(sHeroName, sAuthUser, sAuthPass);
        int status = response.getStatusCode();
        String sResponseBody = response.getBody().asString();
        Assert.assertEquals(status, 200, "Wrong Response Status Code in checkIfHeroExists(" + sHeroName + ")! Response Body: " + sResponseBody);
        if (!(sResponseBody.equalsIgnoreCase("true") || sResponseBody.equalsIgnoreCase("false"))) {
            Assert.fail("Cannot convert response body '" + sResponseBody + "' to boolean value!");
        }
        return Boolean.parseBoolean(sResponseBody);
    }

    public static boolean checkIfHeroExists(String sHeroName) {
        return checkIfHeroExists(sHeroName, ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    private static Response getHeroApiCall(String sHeroName, String sAuthUser, String sAuthPass) {
        String sApiCall = BASE_URL + ApiCalls.createGetHeroApiCall(sHeroName);
        Response response = null;
        try {
            response = RestAssured.given().auth().basic(sAuthUser, sAuthPass)
                    .headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                    .get(sApiCall);
        } catch (Exception e) {
            Assert.fail("Exception in getHero(" + sHeroName + ") Api Call: " + e.getMessage());
        }
        return response;
    }

    public static String getHeroJsonFormat(String sHeroName, String sAuthUser, String sAuthPass) {
        Assert.assertTrue(checkIfHeroExists(sHeroName, sAuthUser, sAuthPass), "Hero '" + sHeroName + "' doesn't exist!");
        Response response = getHeroApiCall(sHeroName, sAuthUser, sAuthPass);
        int status = response.getStatusCode();
        String sResponseBody = response.getBody().asString();
        Assert.assertEquals(status, 200, "Wrong Response Status Code in getHero(" + sHeroName + ") Api Call! Response Body: " + sResponseBody);
        return sResponseBody;
    }

    public static String getHeroJsonFormat(String sHeroName) {
        return getHeroJsonFormat(sHeroName, ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    public static Hero getHero(String sHeroName, String sAuthUser, String sAuthPass) {
        log.debug("getHero(" + sHeroName + ")");
        String json = getHeroJsonFormat(sHeroName, sAuthUser, sAuthPass);
        Gson gson = new Gson();
        return gson.fromJson(json, Hero.class);
    }

    public static Hero getHero(String sHeroName) {
        return getHero(sHeroName, ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    private static Response postHeroApiCall(Hero hero, String sAuthUser, String sAuthPass) {
        String sApiCall = BASE_URL + ApiCalls.createPostHeroApiCall();
        Response response = null;
        try {
            Gson gson = new Gson();
            String json = gson.toJson(hero, Hero.class);
            response = RestAssured.given().auth().basic(sAuthUser, sAuthPass)
                    .headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                    .body(json)
                    .when().redirects().follow(false)
                    .post(sApiCall);
        } catch (Exception e) {
            Assert.fail("Exception in postHero(" + hero.getHeroName() + ") Api Call: " + e.getMessage());
        }
        return response;
    }

    public static void postHero(Hero hero, String sAuthUser, String sAuthPass) {
        log.debug("postHero(" + hero.getHeroName() + ")");
        Assert.assertFalse(checkIfHeroExists(hero.getHeroName(), sAuthUser, sAuthPass), "Hero '" + hero.getHeroName() + "' already exists!");
        Response response = postHeroApiCall(hero, sAuthUser, sAuthPass);
        int status = response.getStatusCode();
        String sResponseBody = response.getBody().asString();
        Assert.assertEquals(status, 200, "Wrong Response Status Code in postHero(" + hero.getHeroName() + ") Api Call! Response Body: " + sResponseBody);
        log.debug("Hero Created: " + checkIfHeroExists(hero.getHeroName(), sAuthUser, sAuthPass));
    }

    public static void postHero(Hero hero) {
        postHero(hero, ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    private static Response deleteHeroApiCall(String sHeroName, String sAuthUser, String sAuthPass) {
        String sApiCall = BASE_URL + ApiCalls.createDeleteHeroApiCall(sHeroName);
        Response response = null;
        try {
            response = RestAssured.given().auth().basic(sAuthUser, sAuthPass)
                    .headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                    .when().redirects().follow(false)
                    .delete(sApiCall);
        } catch (Exception e) {
            Assert.fail("Exception in deleteHero(" + sHeroName + ") Api Call: " + e.getMessage());
        }
        return response;
    }

    public static void deleteHero(String sHeroName, String sAuthUser, String sAuthPass) {
        log.debug("deleteHero(" + sHeroName + ")");
        Assert.assertTrue(checkIfHeroExists(sHeroName,sAuthUser, sAuthPass), "Hero '" + sHeroName + "' doesn't exist!");
        Response response = deleteHeroApiCall(sHeroName, sAuthUser, sAuthPass);
        int status = response.getStatusCode();
        String sResponseBody = response.getBody().asString();
        Assert.assertEquals(status, 200, "Wrong Response Status Code in deleteHero(" + sHeroName + ") Api Call! Response Body: " + sResponseBody);
        log.debug("Hero Deleted: " + !checkIfHeroExists(sHeroName, sAuthUser, sAuthPass));
    }

    public static void deleteHero(String sHeroName) {
        deleteHero(sHeroName, ADMIN_USERNAME, ADMIN_PASSWORD);
    }

}
