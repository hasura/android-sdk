package io.hasura.sdk.responseConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import io.hasura.sdk.Converter;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraErrorCode;
import io.hasura.sdk.Util;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.exception.HasuraJsonException;
import io.hasura.sdk.model.response.HasuraErrorResponse;

/**
 * Created by jaison on 06/07/17.
 */

abstract class BaseResponseConverter<K> implements Converter<K, HasuraException> {

    private static String TAG = "BaseResponseConverter";
    public Gson gson = new GsonBuilder().create();

    @Override
    public HasuraException fromIOException(IOException e) {
        return new HasuraException(HasuraErrorCode.CONNECTION_ERROR, e);
    }

    @Override
    public HasuraException castException(Exception e) {
        if (e instanceof HasuraException) {
            return (HasuraException) e;
        }
        return new HasuraException(HasuraErrorCode.UNKNOWN, "Unable to cast Exeception : " + e.getMessage());
    }

    //TODO: UGLY -FIX/CHANGE
    protected HasuraException getException(okhttp3.Response response) {
        try {
            HasuraErrorResponse err = Util.parseJson(gson, response, HasuraErrorResponse.class);
            HasuraErrorCode errCode = HasuraErrorCode.getFromCode(err.getCode());
            handleErrorResponse(errCode);
            return new HasuraException(errCode, err.getMessage());
        } catch (HasuraJsonException e) {
            try {
                String responseString = response.body().string();
                return new HasuraException(HasuraErrorCode.JSON_PARSE_ERROR, "JSON Parse Error: For response -> " + responseString);
            } catch (IOException ioE) {
                return new HasuraException(HasuraErrorCode.INTERNAL_ERROR, ioE);
            }
        }
    }

    private void handleErrorResponse(HasuraErrorCode errorCode) {
        switch (errorCode) {
            case UNAUTHORISED:
                Hasura.getClient().getUser().forceLogout();
                break;
            default:
                break;
        }
    }


}
