package io.hasura.sdk.auth;

public class AuthException extends Exception {
    private static final long serialVersionUID = 1;
    private AuthErrorCode code;

    /**
     * Construct a new AuthException with a particular error code.
     *
     * @param theCode    The error code to identify the type of exception.
     * @param theMessage A message describing the error in more detail.
     */
    public AuthException(AuthErrorCode theCode, String theMessage) {
        super(theMessage);
        code = theCode;
    }

    /**
     * Construct a new AuthException with a particular error code.
     *
     * @param theCode The error code to identify the type of exception.
     * @param cause   The cause of the error.
     */
    public AuthException(AuthErrorCode theCode, Throwable cause) {
        super(cause);
        code = theCode;
    }

    /**
     * Access the code for this error.
     *
     * @return The code for this error.
     */
    public AuthErrorCode getCode() {
        return code;
    }

    @Override
    public String toString() {
        String message =
                AuthException.class.getName() + " "
                        + code.toString() + " : " + super.getLocalizedMessage();
        return message;
    }
}
