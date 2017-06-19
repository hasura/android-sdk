package io.hasura.sdk.exception;

import io.hasura.sdk.HasuraErrorCode;

public class HasuraException extends Exception {
    private static final long serialVersionUID = 1;
    private HasuraErrorCode code;

    /**
     * Construct a new HasuraException with a particular error code.
     *
     * @param theCode    The error code to identify the type of exception.
     * @param theMessage A message describing the error in more detail.
     */
    public HasuraException(HasuraErrorCode theCode, String theMessage) {
        super(theMessage);
        code = theCode;
    }

    /**
     * Construct a new HasuraException with a particular error code.
     *
     * @param theCode The error code to identify the type of exception.
     * @param cause   The cause of the error.
     */
    public HasuraException(HasuraErrorCode theCode, Throwable cause) {
        super(cause);
        code = theCode;
    }

    /**
     * Access the code for this error.
     *
     * @return The code for this error.
     */
    public HasuraErrorCode getCode() {
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
