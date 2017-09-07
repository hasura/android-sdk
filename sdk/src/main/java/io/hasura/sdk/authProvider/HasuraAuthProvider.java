package io.hasura.sdk.authProvider;

/**
 * Created by jaison on 06/09/17.
 */

/**
 * This interface should be used to create your own custom Authprovider
**/

public interface HasuraAuthProvider<T> {

    /**
     * @return Expects a string for the provider type
     */
    String getProviderType();

    /**
     * @return Expects the data object for the Auth provider
     */
    T getDataObject();
}
