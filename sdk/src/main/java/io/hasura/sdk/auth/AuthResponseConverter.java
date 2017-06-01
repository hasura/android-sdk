package io.hasura.sdk.auth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Type;

import io.hasura.sdk.auth.response.AuthErrorResponse;
import io.hasura.sdk.auth.service.AuthService;
import io.hasura.sdk.core.Converter;
import io.hasura.sdk.core.HasuraJsonException;
import io.hasura.sdk.core.Util;


public class AuthResponseConverter<T> implements Converter<T, AuthException> {

    private final Type resType;
    private Gson gson = new GsonBuilder().create();

    public AuthResponseConverter(Type resType) {
        this.resType = resType;
    }

    @Override
    public T fromResponse(okhttp3.Response response) throws AuthException {
        int code = response.code();

        try {
            if (code == 200) {
                return Util.parseJson(gson, response, resType);
            } else {
                AuthErrorResponse err = Util.parseJson(gson, response, AuthErrorResponse.class);
                AuthErrorCode errCode = AuthErrorCode.getFromCode(err.getCode());
                throw new AuthException(errCode, err.getMessage());
            }
        } catch (HasuraJsonException e) {
            throw new AuthException(AuthErrorCode.INTERNAL_ERROR, e);
        }
    }

    @Override
    public AuthException fromIOException(IOException e) {
        return new AuthException(AuthErrorCode.CONNECTION_ERROR, e);
    }

    @Override
    public AuthException castException(Exception e) {
        return (AuthException) e;
    }

}
