package io.hasura.sdk;

/**
 * Created by jaison on 06/06/17.
 */

public enum HasuraSocialLoginType {
    FACEBOOK("facebook"),
    GITHUB("github"),
    LINKEDIN("linkedin"),
    GOOGLE("google");

    private String code;

    HasuraSocialLoginType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
