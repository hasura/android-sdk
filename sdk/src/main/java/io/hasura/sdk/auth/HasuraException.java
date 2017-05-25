package io.hasura.sdk.auth;

public class HasuraException extends Exception {
    private static final long serialVersionUID = 1;
    private AuthError code;

    /**
     * Construct a new HasuraException with a particular error code.
     *
     * @param theCode    The error code to identify the type of exception.
     * @param theMessage A message describing the error in more detail.
     */
    public HasuraException(AuthError theCode, String theMessage) {
        super(theMessage);
        code = theCode;
    }

    /**
     * Construct a new HasuraException with a particular error code.
     *
     * @param theCode The error code to identify the type of exception.
     * @param cause   The cause of the error.
     */
    public HasuraException(AuthError theCode, Throwable cause) {
        super(cause);
        code = theCode;
    }

    /**
     * Access the code for this error.
     *
     * @return The code for this error.
     */
    public AuthError getCode() {
        return code;
    }

    @Override
    public String toString() {
        String message =
                HasuraException.class.getName() + " "
                        + code.toString() + " : " + super.getLocalizedMessage();
        return message;
    }
}
