package io.hasura.sdk.auth;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jaison on 29/05/17.
 */

public enum AuthErrorCode {
    CONNECTION_ERROR("connection-error"),
    USER_ALREADY_EXISTS("user-already-exists"),
    INTERNAL_ERROR("internal-error"),
    UNEXPECTED("unexpected"),
    JSON_PARSE_ERROR("json-parse-error"),
    PASSWORD_TOO_SHORT("short-password"),
    MOBILE_EXPECTED("expected-mobile"),
    EMAIL_EXPECTED("expected-email"),
    USERNAME_EXPECTED("expected-username"),
    INVALID_USER("invalid-user"),

    UNKNOWN("unknown"); // For response without a code key TODO: REMOVE if not required

    private String code;
    private static final Map<String, AuthErrorCode> codeValues;

    static {
        codeValues = new HashMap<>();
        for (AuthErrorCode error : AuthErrorCode.values()) {
            codeValues.put(error.code, error);
        }
    }

    AuthErrorCode(String code) {
        this.code = code;
    }

    public static AuthErrorCode getFromCode(String code) {
        if (code == null) {
            return UNKNOWN;
        }
        return codeValues.get(code) != null ? codeValues.get(code) : UNKNOWN;
    }

    public String getCode() {
        return code;
    }

}
