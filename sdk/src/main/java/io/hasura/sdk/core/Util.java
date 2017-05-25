package io.hasura.sdk.core;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.lang.reflect.Type;

public class Util {
    public static <R> R parseJson(Gson gson,
            okhttp3.Response response, Type bodyType) throws HasuraJsonException {
        int code = response.code();
        try {
            String rawBody = response.body().string();
            Log.i("Response",rawBody);
            return gson.fromJson(rawBody, bodyType);
        } catch (JsonSyntaxException e) {
            String msg
                    = "FATAL : JSON structure not as expected. Schema changed maybe? : "
                    + e.getMessage();
            throw new HasuraJsonException(code, msg, e);
        } catch (JsonParseException e) {
            String msg = "FATAL : Server didn't return valid JSON : " + e.getMessage();
            throw new HasuraJsonException(code, msg, e);
        } catch (IOException e) {
            String msg = "FATAL : Decoding response body failed : " + e.getMessage();
            throw new HasuraJsonException(code, msg, e);
        }
    }
}
