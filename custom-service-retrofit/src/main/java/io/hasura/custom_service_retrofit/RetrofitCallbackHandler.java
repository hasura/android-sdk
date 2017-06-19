package io.hasura.custom_service_retrofit;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import io.hasura.sdk.HasuraErrorCode;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.model.response.HasuraErrorResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jaison on 11/06/17.
 */

public abstract class RetrofitCallbackHandler<T> implements Callback<T>, io.hasura.sdk.Callback<T, HasuraException> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onSuccess(response.body());
        } else {
            try {
                HasuraErrorResponse err = new Gson().fromJson(response.errorBody().string(), HasuraErrorResponse.class);
                HasuraErrorCode errCode = HasuraErrorCode.getFromCode(err.getCode());
                onFailure(new HasuraException(errCode, err.getMessage()));
            } catch (JsonSyntaxException e) {
                onFailure(new HasuraException(HasuraErrorCode.JSON_PARSE_ERROR, e.getMessage()));
            } catch (IOException e) {
                onFailure(new HasuraException(HasuraErrorCode.INTERNAL_ERROR, e.getMessage()));}
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFailure(new HasuraException(HasuraErrorCode.CONNECTION_ERROR, t.getMessage()));
    }

}
