package io.hasura.sdk;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HasuraTokenInterceptor implements Interceptor {

    private String authToken;
    private String role;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer " + authToken)
                .addHeader("X-Hasura-Role", role)
                .build();
        return chain.proceed(newRequest);
    }
}
