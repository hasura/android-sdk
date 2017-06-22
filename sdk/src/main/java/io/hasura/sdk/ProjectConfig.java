package io.hasura.sdk;

import io.hasura.sdk.exception.HasuraInitException;

/**
 * Created by jaison on 19/06/17.
 */

public class ProjectConfig {

    private String defaultRole;
    private String protocol;
    private String baseDomain;
    private String projectName;
    private String version;

    private ProjectConfig() {
    }

    private ProjectConfig(Builder builder) {
        this.projectName = builder.projectName;
        this.protocol = builder.isEnabledOverHttp ? "http" : "https";
        this.defaultRole = builder.defaultRole;
        this.baseDomain = builder.baseDomain == null ? projectName + ".hasura-app.io" : builder.baseDomain;
        this.version = "v" + builder.apiVersion;
    }

    public String getCustomServiceUrl(String customServiceName) {
        return getUrl(protocol, customServiceName, baseDomain);
    }

    public String getUploadFileUrl(String fileName) {
        return getUrl(protocol, SERVICE.FILESTORE, baseDomain + "/") + version + "/file/" + fileName;
    }

    public String getDownloadFileUrl(String fileId) {
        return getUrl(protocol, SERVICE.FILESTORE, baseDomain + "/") + version + "/file/" + fileId;
    }

    public String getAuthUrl() {
        return getUrl(protocol, SERVICE.AUTH, baseDomain + "/");
    }

    public String getQueryUrl() {
        return getDataUrl() + version + "/query";
    }

    public String getQueryTemplateUrl(String templateName) {
        return getDataUrl() + version + "/template/" + templateName;
    }

    private String getDataUrl() {
        return getUrl(protocol, SERVICE.DATA, baseDomain + "/");
    }

    public String getDefaultRole() {
        return defaultRole;
    }

    private static String getUrl(String protocol, String serviceName, String baseDomain) {
        return protocol + "://" + serviceName + "." + baseDomain;
    }

    private static class DEFAULTS {
        static Boolean IS_ENABLED_OVER_HTTP = false;
        static String DEFAULT_ROLE = "user";
        static Integer API_VERSION = 1;
    }

    private static class SERVICE {
        static String AUTH = "auth";
        static String DATA = "data";
        static String FILESTORE = "filestore";
    }

    public static class Builder {

        String projectName;
        String baseDomain;
        Boolean isEnabledOverHttp = DEFAULTS.IS_ENABLED_OVER_HTTP;
        String defaultRole = DEFAULTS.DEFAULT_ROLE;
        Integer apiVersion = DEFAULTS.API_VERSION;

        public Builder setProjectName(String projectName) {
            this.projectName = projectName;
            return this;
        }

        public Builder setCustomBaseDomain(String baseDomain) throws HasuraInitException {
            if (baseDomain.endsWith("/"))
                throw new HasuraInitException("Base domain must not end with a /");
            this.baseDomain = baseDomain;
            return this;
        }

        public Builder enableOverHttp() {
            this.isEnabledOverHttp = true;
            return this;
        }

        public Builder setDefaultRole(String role) {
            this.defaultRole = role;
            return this;
        }

        public Builder setApiVersion(int version) {
            this.apiVersion = version;
            return this;
        }

        public ProjectConfig build() {
            return new ProjectConfig(this);
        }

    }
}
