package io.hasura.sdk;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.hasura.sdk.response.AuthErrorResponse;
import okhttp3.Response;

/**
 * Created by jaison on 10/06/17.
 */

public class HasuraListResponseConverter<K> implements Converter<List<K>, HasuraException> {

    private Class<K> clazz;
    private Gson gson = new GsonBuilder().create();

    public HasuraListResponseConverter(Class<K> clazz) {
        this.clazz = clazz;
    }

    @Override
    public List<K> fromResponse(Response response) throws HasuraException {
        int code = response.code();

        try {
            if (code == 200) {
                return Util.parseJsonArray(gson,response, clazz);
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
