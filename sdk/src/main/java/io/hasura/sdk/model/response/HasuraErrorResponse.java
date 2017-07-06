package io.hasura.sdk.model.response;

public class HasuraErrorResponse {
    private String code;
    private String message;


    //TODO: This is a hack until error responses are standard
    public String getCode() {
        if (code == null) {
            if (message != null) {
                if (message.equalsIgnoreCase("invalid authorization token"))
                    code = "session-expired";
            }
        }
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}