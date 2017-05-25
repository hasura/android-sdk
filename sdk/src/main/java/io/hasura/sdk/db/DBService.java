package io.hasura.sdk.db;

import io.hasura.sdk.auth.HasuraException;
import io.hasura.sdk.core.Call;
import io.hasura.sdk.core.HasuraService;
import io.hasura.sdk.utils.HasuraConfig;

/**
 * Created by jaison on 23/05/17.
 */

public class DBService extends HasuraService {

    public DBService(String url) {
        super(url);
    }

    public <T> Call<T, HasuraException> query(Object requestBody) {
        return mkPostCall(HasuraConfig.URL.QUERY, requestBody);
    }


}
