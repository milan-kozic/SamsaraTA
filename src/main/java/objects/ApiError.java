package objects;

import utils.DateTimeUtils;

import java.util.Date;

public class ApiError {

    private Long timestamp;
    private Integer status;
    private String error;
    private String exception;
    private String message;
    private String path;

    public Date getTimestamp() {
        if(timestamp == null) {
            return null;
        } else {
            return DateTimeUtils.getDateTime(timestamp);
        }
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getException() {
        return exception;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "ApiError {"
                + "Status: " + getStatus() + ", "
                + "Error: " + getError() + ", "
                + "Exception: " + getException() + ", "
                + "Message: " + getMessage() + ", "
                + "Path: " + getPath() + ", "
                + "Timestamp: " + getTimestamp() + "}";
    }
}
