package io.hasura.sdk.model.request;

public class SocialLoginRequest {
    String provider;
    String accessToken;
    String idToken;

    public SocialLoginRequest(String provider, String token) {
        this.provider = provider;
        if(provider == "google") {
          this.idToken = token;
        }
        else {
          this.accessToken = token;
        }
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setAccessToken(String token) {
        this.accessToken = token;
    }

    public void setIdToken(String token) {
        this.idToken = token;
    }

    public String prepareRequestURL() {
        if(idToken != null) {
          return provider + "/authenticate?id_token=" + idToken;
        }
        else {
          return provider + "/authenticate?access_token=" + accessToken;
        }
    }
}
