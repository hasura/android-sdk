package io.hasura.sdk.responseConverter;

import java.lang.reflect.Type;

import io.hasura.sdk.HasuraErrorCode;
import io.hasura.sdk.Util;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.exception.HasuraJsonException;


public class HasuraResponseConverter<T> extends BaseResponseConverter<T> {

    private Class<T> clazz;
    private Type responseType;

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
                throw getException(response);
            }
        } catch (HasuraJsonException e) {
            throw new HasuraException(HasuraErrorCode.INTERNAL_ERROR, e);
        }
    }
}
