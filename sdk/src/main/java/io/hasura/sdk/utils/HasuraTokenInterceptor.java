package io.hasura.sdk.utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HasuraTokenInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response;
        String session = Hasura.getUserSessionId();
        if (session == null) {
            response = chain.proceed(request);
        } else {
            Request newRequest = request.newBuilder()
                    .addHeader("Authorization", "Bearer " + session)
                    .build();
            response = chain.proceed(newRequest);
        }
        return response;
    }
}
