package io.hasura.sdk;

import android.util.Log;

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
        Request.Builder builder = request.newBuilder();

        Log.i("Request","Auth " + authToken);
        Log.i("Request","Role " + role);

        if (authToken != null) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        if (role != null) {
            builder.addHeader("X-HasuraClient-Role", role);
        }
        return chain.proceed(builder.build());
    }

    @Override
    public String toString() {
        return "HasuraTokenInterceptor{" +
                "authToken='" + authToken + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
