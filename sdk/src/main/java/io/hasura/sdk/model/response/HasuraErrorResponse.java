package io.hasura.sdk.model.response;

import io.hasura.sdk.HasuraErrorCode;

public class HasuraErrorResponse {
    private String code;
    private String message;

    private String path;
    private String error;


    //TODO: This is a hack until error responses are standard
    public String getCode() {
        if (code == null) {
            if (message != null) {
                if (message.equalsIgnoreCase("invalid authorization token"))
                    code = HasuraErrorCode.UNAUTHORISED.getCode();
            } else code = HasuraErrorCode.BAD_REQUEST.getCode();
        }
        return this.code;
    }

    public String getMessage() {
        if (message == null) {
            message = "Error: ";
            if (path != null) {
                message = message + "path: " + path;
            }
            if (error != null) {
                message = message + ", message: " + error;
            }
        }
        return message;
    }
}