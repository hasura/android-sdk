package io.hasura.sdk.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Type;

import io.hasura.sdk.auth.response.AuthErrorResponse;


public class HasuraResponseConverter<T> implements Converter<T, HasuraException> {

    private final Type resType;
    private Gson gson = new GsonBuilder().create();

    public HasuraResponseConverter(Type resType) {
        this.resType = resType;
    }

    @Override
    public T fromResponse(okhttp3.Response response) throws HasuraException {
        int code = response.code();

        try {
            if (code == 200) {
                return Util.parseJson(gson, response, resType);
            } else {
                AuthErrorResponse err = Util.parseJson(gson, response, AuthErrorResponse.class);
                HasuraErrorCode errCode = HasuraErrorCode.getFromCode(err.getCode());
                throw new HasuraException(errCode, err.getMessage());
            }
        } catch (HasuraJsonException e) {
            throw new HasuraException(HasuraErrorCode.INTERNAL_ERROR, e);
        }
    }

    @Override
    public HasuraException fromIOException(IOException e) {
        return new HasuraException(HasuraErrorCode.CONNECTION_ERROR, e);
    }

    @Override
    public HasuraException castException(Exception e) {
        return (HasuraException) e;
    }

}
