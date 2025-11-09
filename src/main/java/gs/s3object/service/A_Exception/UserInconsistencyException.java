package gs.s3object.service.A_Exception;

public class UserInconsistencyException extends RuntimeException {

    private final String userLogin; // The login or identifier of the user with inconsistency
    private final String additionalInfo; // Any additional information (optional)

    // Constructor with just the message
    public UserInconsistencyException(String message) {
        super(message);
        this.userLogin = null;
        this.additionalInfo = null;
    }

    // Constructor with message and user login details
    public UserInconsistencyException(String message, String userLogin) {
        super(message);
        this.userLogin = userLogin;
        this.additionalInfo = null;
    }

    // Constructor with message, user login, and additional info
    public UserInconsistencyException(String message, String userLogin, String additionalInfo) {
        super(message);
        this.userLogin = userLogin;
        this.additionalInfo = additionalInfo;
    }

    // Getters for the additional fields
    public String getUserLogin() {
        return userLogin;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    @Override
    public String toString() {
        return String.format("UserInconsistencyException: %s (UserLogin: %s, Info: %s)",
                getMessage(), userLogin, additionalInfo);
    }
}