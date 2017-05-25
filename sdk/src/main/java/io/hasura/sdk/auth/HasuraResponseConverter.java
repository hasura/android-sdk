package io.hasura.sdk.auth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Type;

import io.hasura.sdk.auth.response.HasuraSession;
import io.hasura.sdk.core.Converter;
import io.hasura.sdk.core.HasuraJsonException;
import io.hasura.sdk.core.Util;
import io.hasura.sdk.utils.HasuraSessionStore;

public class HasuraResponseConverter<T> implements Converter<T, HasuraException> {

    private final Type resType;
    public static final Gson gson = new GsonBuilder().create();

    public HasuraResponseConverter(Type resType) {
        this.resType = resType;
    }

    @Override
    public T fromResponse(okhttp3.Response response) throws HasuraException {
        int code = response.code();
        try {
            if (code == 200) {
                try {
                    HasuraSession hasuraSession = gson.fromJson(response.body().string(), HasuraSession.class);
                    HasuraSessionStore.saveSession(hasuraSession);
                } catch (IOException e) {
                    //Ignore
                }
                return Util.parseJson(gson, response, resType);
            } else {
                AuthErrorResponse err = Util.parseJson(gson, response, AuthErrorResponse.class);
                AuthError errCode;
                switch (code) {
                    case 400:
                        errCode = AuthError.BAD_REQUEST;
                        break;
                    case 401:
                        errCode = AuthError.UNAUTHORIZED;
                        HasuraSessionStore.clearSession();
                        break;
                    case 402:
                        errCode = AuthError.REQUEST_FAILED;
                        break;
                    case 403:
                        errCode = AuthError.INVALID_SESSION;
                        break;
                    case 500:
                        errCode = AuthError.INTERNAL_ERROR;
                        break;
                    default:
                        errCode = AuthError.UNEXPECTED_CODE;
                        break;
                }
                throw new HasuraException(errCode, err.getMessage());
            }
        } catch (HasuraJsonException e) {
            throw new HasuraException(AuthError.INTERNAL_ERROR, e);
        }
    }

    @Override
    public HasuraException fromIOException(IOException e) {
        return new HasuraException(AuthError.CONNECTION_ERROR, e);
    }

    @Override
    public HasuraException castException(Exception e) {
        return (HasuraException) e;
    }

    static class AuthErrorResponse {
        private int code;
        private String message;

        public int getCode() {
            return this.code;
        }

        public String getMessage() {
            return this.message;
        }
    }
}
