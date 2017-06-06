package io.hasura.sdk.core;

import java.io.IOException;

import io.hasura.sdk.core.Hasura;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HasuraTokenInterceptor implements Interceptor {

    private String authToken;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer " + authToken)
                .build();
        return chain.proceed(newRequest);
    }
}
