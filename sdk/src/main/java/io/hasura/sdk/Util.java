package io.hasura.sdk;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.hasura.sdk.exception.HasuraJsonException;
import okhttp3.Response;

public class Util {

    public static <R> List<R> parseJsonArray(Gson gson, Response response, Class<R> clazz) throws HasuraJsonException {
        int code = response.code();
        try {
            List<R> responseList = new ArrayList<>();
            String rawBody = response.body().string();
            Log.i("Response", rawBody);

            JsonArray array = new JsonParser().parse(rawBody).getAsJsonArray();
            for (JsonElement jsonElement : array) {
                R r = gson.fromJson(jsonElement, clazz);
                responseList.add(r);
            }
            return responseList;
        } catch (JsonSyntaxException e) {
            String msg = "JSON structure not as expected. Schema changed maybe? : " + e.getMessage();
            throw new HasuraJsonException(code, msg, e);
        } catch (JsonParseException e) {
            String msg = "Server didn't return valid JSON : " + e.getMessage();
            throw new HasuraJsonException(code, msg, e);
        } catch (IOException e) {
            String msg = "Decoding response body failed : " + e.getMessage();
            throw new HasuraJsonException(code, msg, e);
        }
    }

    public static <R> R parseJson(Gson gson, Response response, Class<R> clazz) throws HasuraJsonException {
        int code = response.code();
        try {
            String rawBody = response.body().string();
            Log.i("Response", rawBody);
            return gson.fromJson(rawBody, clazz);
        } catch (JsonSyntaxException e) {
            String msg = "JSON structure not as expected. Schema changed maybe? : " + e.getMessage();
            throw new HasuraJsonException(code, msg, e);
        } catch (JsonParseException e) {
            String msg = "Server didn't return valid JSON : " + e.getMessage();
            throw new HasuraJsonException(code, msg, e);
        } catch (IOException e) {
            String msg = "Decoding response body failed : " + e.getMessage();
            throw new HasuraJsonException(code, msg, e);
        }
    }

    public static <R> R parseJson(Gson gson, Response response, Type responseType) throws HasuraJsonException {
        int code = response.code();
        try {
            String rawBody = response.body().string();
            Log.i("Response", rawBody);
            return gson.fromJson(rawBody, responseType);
        } catch (JsonSyntaxException e) {
            String msg = "JSON structure not as expected. Schema changed maybe? : " + e.getMessage();
            throw new HasuraJsonException(code, msg, e);
        } catch (JsonParseException e) {
            String msg = "Server didn't return valid JSON : " + e.getMessage();
            throw new HasuraJsonException(code, msg, e);
        } catch (IOException e) {
            String msg = "Decoding response body failed : " + e.getMessage();
            throw new HasuraJsonException(code, msg, e);
        }
    }

}
