package io.hasura.sdk.service;

import java.util.HashMap;

import io.hasura.sdk.HasuraTokenInterceptor;

/**
 * Created by jaison on 11/06/17.
 */

public interface CustomServiceBuilder {
    <K> K getApiInterface(HasuraTokenInterceptor hasuraTokenInterceptor, String baseUrl, final HashMap<String, String> additionalHeaders, Class<K> clazz);
}
