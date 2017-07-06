package io.hasura.sdk.responseConverter;

import java.util.List;

import io.hasura.sdk.HasuraErrorCode;
import io.hasura.sdk.Util;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.exception.HasuraJsonException;
import io.hasura.sdk.model.response.HasuraErrorResponse;
import okhttp3.Response;

/**
 * Created by jaison on 10/06/17.
 */

public class HasuraListResponseConverter<K> extends BaseResponseConverter<List<K>> {

    private Class<K> clazz;

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
                throw getException(response);
            }
        } catch (HasuraJsonException e) {
            throw new HasuraException(HasuraErrorCode.INTERNAL_ERROR, e);
        }
    }

}
