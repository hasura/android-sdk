package io.hasura.sdk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Type;

import io.hasura.sdk.response.HasuraErrorResponse;


public class HasuraResponseConverter<T> implements Converter<T, HasuraException> {

    private Class<T> clazz;
    private Type responseType;
    private Gson gson = new GsonBuilder().create();

    public HasuraResponseConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    public HasuraResponseConverter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public T fromResponse(okhttp3.Response response) throws HasuraException {
        int code = response.code();

        try {
            if (code == 200) {
                if (clazz != null)
                    return Util.parseJson(gson, response, clazz);
                else return Util.parseJson(gson, response, responseType);
            } else {
                HasuraErrorResponse err = Util.parseJson(gson, response, HasuraErrorResponse.class);
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
