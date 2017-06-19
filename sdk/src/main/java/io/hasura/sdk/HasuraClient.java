package io.hasura.sdk;

import java.util.HashMap;
import java.util.Map;

import io.hasura.sdk.query.HasuraQuery;
import io.hasura.sdk.service.CustomService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by jaison on 23/01/17.
 */

/**
 * SDK Init/≥¬¬¬
 * HasuraUser AKA AuthService
 * DataService
 * QueryTemplateService
 * CustomService
 **/

public class HasuraClient {

    private HasuraUser currentUser;
    private ProjectConfig projectConfig;
    private HashMap<Class, CustomService> customServiceMap = new HashMap<>();
    private Boolean shouldEnableLogs;

    private HashMap<String, HasuraTokenInterceptor> interceptorRoleMap = new HashMap<>();
    private HashMap<String, OkHttpClient> clientRoleMap = new HashMap<>();


    private HasuraUser.StateChangeListener userStateChangeListener = new HasuraUser.StateChangeListener() {

        @Override
        public void onAuthTokenChanged(String authToken) {
            for (Map.Entry<String, HasuraTokenInterceptor> entry : interceptorRoleMap.entrySet()) {
                entry.getValue().setAuthToken(authToken);
            }
        }

        @Override
        public void onRolesChanged(String[] roles) {

        }
    };

    private HasuraClient() {

    }

    private HasuraClient(Builder builder) {
        this.customServiceMap = builder.customServiceMap;
        this.shouldEnableLogs = builder.shouldEnableLogs;
        this.projectConfig = builder.projectConfig;
        this.currentUser = new HasuraUser.Builder()
                .setAuthUrl(projectConfig.getAuthUrl())
                .setStateChangeListener(userStateChangeListener)
                .shouldEnableLogging(builder.shouldEnableLogs)
                .build();

        //update the user with saved data
        HasuraSessionStore.updateUserWithSavedData(this.currentUser);
    }

    /**************************************BUILDER**************************************/

    public final static class Builder {

        private HashMap<Class, CustomService> customServiceMap = new HashMap<>();
        private Boolean shouldEnableLogs;
        private ProjectConfig projectConfig;

        public Builder setCustomServices(HashMap<Class, CustomService> customServiceMap) {
            this.customServiceMap = customServiceMap;
            return this;
        }

        public Builder shouldEnableLogs(Boolean shouldEnableLogs) {
            this.shouldEnableLogs = shouldEnableLogs;
            return this;
        }

        public Builder setProjectConfig(ProjectConfig config) {
            this.projectConfig = config;
            return this;
        }

        public HasuraClient build() {
            return new HasuraClient(this);
        }
    }


    /**************************************HASURA USER**************************************/

    public HasuraUser getUser() {
        return currentUser;
    }

    /**************************************DATA SERVICE*********************************************/

    private HasuraTokenInterceptor getTokenInterceptorForRole(String role) {
        if (interceptorRoleMap.containsKey(role)) {
            return interceptorRoleMap.get(role);
        }
        HasuraTokenInterceptor interceptor = new HasuraTokenInterceptor();
        interceptor.setAuthToken(currentUser.getAuthToken());
        interceptorRoleMap.put(role, interceptor);
        return interceptor;
    }

    private OkHttpClient getClientForRole(String role) {
        if (clientRoleMap.containsKey(role)) {
            return clientRoleMap.get(role);
        }

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        if (shouldEnableLogs) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(loggingInterceptor);
        }

        clientBuilder.addInterceptor(getTokenInterceptorForRole(role));

        OkHttpClient client = clientBuilder.build();

        clientRoleMap.put(role, client);
        return client;
    }

    public ApiService asRole(String role) {
        return new ApiService(role);
    }

    public HasuraQuery.Builder useDataService() {
        return new ApiService(projectConfig.getDefaultRole()).useDataService();
    }

    public HasuraQuery.Builder useQueryTemplateService(String templateName) {
        return new ApiService(projectConfig.getDefaultRole()).useQueryTemplateService(templateName);
    }

    public <K> K useCustomService(Class<K> clzz) {
        return new ApiService(projectConfig.getDefaultRole()).useCustomService(clzz);
    }

    public class ApiService {

        String role;

        public ApiService(String role) {
            this.role = role;
        }

        public HasuraQuery.Builder useDataService() {
            return new HasuraQuery.Builder(getClientForRole(role)).setBaseUrl(projectConfig.getQueryUrl());
        }

        public HasuraQuery.Builder useQueryTemplateService(String templateName) {
            return new HasuraQuery.Builder(getClientForRole(role)).setBaseUrl(projectConfig.getQueryTemplateUrl(templateName));
        }

        public <K> K useCustomService(Class<K> clzz) {
            return clzz.cast(customServiceMap.get(clzz).getInterface(getTokenInterceptorForRole(role)));
        }

    }

}
