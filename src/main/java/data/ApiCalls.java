package data;

public final class ApiCalls {
    private static final String CHECK_IF_USER_EXISTS = "/api/users/exists/";
    private static final String GET_USER = "/api/users/findByUsername/";
    private static final String POST_USER = "/api/users/add";
    private static final String DELETE_USER = "/api/users/deleteByUsername/";

    public static String createCheckIfUserExistsApiCall(String sUsername) {
        return CHECK_IF_USER_EXISTS + sUsername;
    }

    public static String createGetUserApiCall(String sUsername) {
        return GET_USER + sUsername;
    }

    public static String createPostUserApiCall() {
        return POST_USER;
    }

    public static String createDeleteUserApiCall(String sUsername) {
        return DELETE_USER + sUsername;
    }
}
