package io.hasura.sdk.authProvider;

import io.hasura.sdk.HasuraUser;

/**
 * Created by jaison on 06/09/17.
 */

public class UsernameAuthProvider implements HasuraAuthProvider<UsernamePasswordRecord> {

    String username, password;

    private static final String TYPE = "username";

    public UsernameAuthProvider(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String getProviderType() {
        return TYPE;
    }

    @Override
    public UsernamePasswordRecord getDataObject() {
        return new UsernamePasswordRecord(username, password);
    }
}
