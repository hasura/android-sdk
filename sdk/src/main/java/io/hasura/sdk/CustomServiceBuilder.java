package io.hasura.sdk;

import java.util.HashMap;

/**
 * Created by jaison on 11/06/17.
 */

public interface CustomServiceBuilder {
    <K> K getApiInterface(HasuraTokenInterceptor hasuraTokenInterceptor, String baseUrl, final HashMap<String, String> additionalHeaders, Class<K> clazz);
}
