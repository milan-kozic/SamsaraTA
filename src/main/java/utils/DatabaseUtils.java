package utils;

import objects.DatabaseHero;
import objects.DatabaseUser;
import objects.Hero;
import objects.User;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.testng.Assert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseUtils extends LoggerUtils {

    private static final String DATA_SOURCE_URL = PropertiesUtils.getDataSourceUrl();
    private static final String ROOT_USERNAME = PropertiesUtils.getRootUsername();
    private static final String ROOT_PASSWORD = PropertiesUtils.getRootPassword();

    public static String getUserID(String sUsername) {
        log.trace("getUserID(" + sUsername + ")");
        String sqlQuery = "SELECT user_id FROM users WHERE username = ?";

        Connection connection = null;
        QueryRunner run = new QueryRunner();
        ScalarHandler<String> scalarHandler = new ScalarHandler<>();
        String result = null;

        try {
            // Register Driver
            // DbUtils.loadDriver("com.mysql.jdbc.Driver");
            // Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DATA_SOURCE_URL, ROOT_USERNAME, ROOT_PASSWORD);
            result = run.query(connection, sqlQuery, scalarHandler, sUsername);
        } catch (Exception e) {
            Assert.fail("Exception in method getUserID(" + sUsername + ") while trying to connect to database. Message: " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    if (!connection.isClosed()) {
                        DbUtils.close(connection);
                    }
                }
            } catch (SQLException e) {
                Assert.fail("Exception in method getUserID(" + sUsername + ") while trying to close database. Message: " + e.getMessage());
            }
        }
       return result;
    }

    public static String getUsername(String sUserID) {
        log.trace("getUsername(" + sUserID + ")");
        String sqlQuery = "SELECT username FROM users WHERE user_id = ?";

        Connection connection = null;
        QueryRunner run = new QueryRunner();
        ScalarHandler<String> scalarHandler = new ScalarHandler<>();
        String result = null;

        try {
            connection = DriverManager.getConnection(DATA_SOURCE_URL, ROOT_USERNAME, ROOT_PASSWORD);
            result = run.query(connection, sqlQuery, scalarHandler, sUserID);
        } catch (Exception e) {
            Assert.fail("Exception in method getUsername(" + sUserID + ") while trying to connect to database. Message: " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    if (!connection.isClosed()) {
                        DbUtils.close(connection);
                    }
                }
            } catch (SQLException e) {
                Assert.fail("Exception in method getUsername(" + sUserID + ") while trying to close database. Message: " + e.getMessage());
            }
        }
        return result;
    }

    public static List<String> getAllUsernames() {
        log.trace("getUserID()");
        String sqlQuery = "SELECT username FROM users";

        Connection connection = null;
        QueryRunner run = new QueryRunner();
        ColumnListHandler<String> columnListHandler = new ColumnListHandler<>();
        List<String> result = null;

        try {
            connection = DriverManager.getConnection(DATA_SOURCE_URL, ROOT_USERNAME, ROOT_PASSWORD);
            result = run.query(connection, sqlQuery, columnListHandler);
        } catch (Exception e) {
            Assert.fail("Exception in method getAllUsernames() while trying to connect to database. Message: " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    if (!connection.isClosed()) {
                        DbUtils.close(connection);
                    }
                }
            } catch (SQLException e) {
                Assert.fail("Exception in method getAllUsernames() while trying to close database. Message: " + e.getMessage());
            }
        }
        return result;
    }

    public static DatabaseUser getDatabaseUser (String sUsername) {
        log.trace("getDatabaseUser(" + sUsername + ")");
        String sqlQuery = "SELECT * FROM users WHERE username = ?";

        Connection connection = null;
        QueryRunner run = new QueryRunner();
        ResultSetHandler<DatabaseUser> resultSetHandler = new BeanHandler<>(DatabaseUser.class);
        DatabaseUser result = null;

        try {
            connection = DriverManager.getConnection(DATA_SOURCE_URL, ROOT_USERNAME, ROOT_PASSWORD);
            result = run.query(connection, sqlQuery, resultSetHandler, sUsername);
        } catch (Exception e) {
            Assert.fail("Exception in method getDatabaseUser(" + sUsername + ") while trying to connect to database. Message: " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    if (!connection.isClosed()) {
                        DbUtils.close(connection);
                    }
                }
            } catch (SQLException e) {
                Assert.fail("Exception in method getDatabaseUser(" + sUsername + ") while trying to close database. Message: " + e.getMessage());
            }
        }
        return result;
    }

    public static List<DatabaseHero> getDatabaseHeroesForUser(String sUserID) {
        log.trace("getDatabaseHeroesForUser(" + sUserID + ")");
        String sqlQuery = "SELECT * FROM heroes WHERE fk_user_id = ?";

        Connection connection = null;
        QueryRunner run = new QueryRunner();
        ResultSetHandler<List<DatabaseHero>> resultSetHandler = new BeanListHandler<>(DatabaseHero.class);
        List<DatabaseHero> result  = null;

        try {
            connection = DriverManager.getConnection(DATA_SOURCE_URL, ROOT_USERNAME, ROOT_PASSWORD);
            result = run.query(connection, sqlQuery, resultSetHandler, sUserID);
        } catch (Exception e) {
            Assert.fail("Exception in method getDatabaseHeroesForUser(" + sUserID + ") while trying to connect to database. Message: " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    if (!connection.isClosed()) {
                        DbUtils.close(connection);
                    }
                }
            } catch (SQLException e) {
                Assert.fail("Exception in method getDatabaseHeroesForUser(" + sUserID + ") while trying to close database. Message: " + e.getMessage());
            }
        }
        return result;
    }

    private static User assembleUser (DatabaseUser databaseUser) {
        String sUsername = databaseUser.getUsername();
        String sPassword = databaseUser.getPassword();
        String sEmail = databaseUser.getEmail();
        String sFirstName = databaseUser.getFirst_name();
        String sLastName = databaseUser.getLast_name();
        String sAbout = databaseUser.getAbout();
        String sSecretQuestion = databaseUser.getSecret_question();
        String sSecretAnswer = databaseUser.getSecret_answer();
        Date dCreatedAt = databaseUser.getCreated();
        List<Hero> heroes = new ArrayList<>();
        List<DatabaseHero> databaseHeroes = DatabaseUtils.getDatabaseHeroesForUser(databaseUser.getUser_id());
        for(DatabaseHero h : databaseHeroes) {
            Hero hero = assembleHero(h);
            heroes.add(hero);
        }
        return new User(sUsername, sPassword, sEmail, sFirstName, sLastName, sAbout, sSecretQuestion, sSecretAnswer, dCreatedAt, heroes);
    }

    private static Hero assembleHero(DatabaseHero databaseHero) {
        String sHeroName = databaseHero.getName();
        String sHeroClass = databaseHero.getType();
        int iHeroLevel = databaseHero.getLevel();
        Date dCreatedAt = databaseHero.getCreated();
        String sUsername = DatabaseUtils.getUsername(databaseHero.getFk_user_id());
        return new Hero(sHeroName, sHeroClass, iHeroLevel, sUsername, dCreatedAt);
    }

    public static User getUser(String sUsername) {
        DatabaseUser databaseUser = DatabaseUtils.getDatabaseUser(sUsername);
        return assembleUser(databaseUser);
    }
}
