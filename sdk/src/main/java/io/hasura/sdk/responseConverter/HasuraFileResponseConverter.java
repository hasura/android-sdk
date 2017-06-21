package io.hasura.sdk.responseConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import io.hasura.sdk.Converter;
import io.hasura.sdk.HasuraErrorCode;
import io.hasura.sdk.Util;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.exception.HasuraJsonException;
import io.hasura.sdk.model.response.HasuraErrorResponse;
import okhttp3.Response;

/**
 * Created by jaison on 21/06/17.
 */

public class HasuraFileResponseConverter implements Converter<byte[], HasuraException> {

    private Gson gson = new GsonBuilder().create();

    @Override
    public byte[] fromResponse(okhttp3.Response response) throws HasuraException {
        int code = response.code();

        try {
            if (code == 200) {
                return response.body().bytes();
            } else {
                HasuraErrorResponse err = Util.parseJson(gson, response, HasuraErrorResponse.class);
                HasuraErrorCode errCode = HasuraErrorCode.getFromCode(err.getCode());
                throw new HasuraException(errCode, err.getMessage());
            }
        } catch (HasuraJsonException e) {
            e.printStackTrace();
            throw new HasuraException(HasuraErrorCode.INTERNAL_ERROR, e);
        } catch (IOException e) {
            e.printStackTrace();
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
