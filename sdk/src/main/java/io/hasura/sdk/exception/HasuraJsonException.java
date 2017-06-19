package io.hasura.sdk.exception;

public class HasuraJsonException extends Exception {
    private static final long serialVersionUID = 1;
    private int code;

    /**
     * Construct a new HasuraJsonException with a particular error code.
     *
     * @param theCode    The error code to identify the type of exception.
     * @param theMessage A message describing the error in more detail.
     */
    public HasuraJsonException(int theCode, String theMessage) {
        super(theMessage);
        code = theCode;
    }

    /**
     * Construct a new HasuraJsonException with an external cause.
     *
     * @param message A message describing the error in more detail.
     * @param cause   The cause of the error.
     */
    public HasuraJsonException(int theCode, String message, Throwable cause) {
        super(message, cause);
        code = theCode;
    }

    /**
     * Construct a new HasuraJsonException with an external cause.
     *
     * @param cause The cause of the error.
     */
    public HasuraJsonException(Throwable cause) {
        super(cause);
        code = 0;
    }

    /**
     * Access the code for this error.
     *
     * @return The numerical code for this error.
     */
    public int getCode() {
        return code;
    }
}
