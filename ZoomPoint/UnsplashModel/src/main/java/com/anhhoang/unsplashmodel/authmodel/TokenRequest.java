package com.anhhoang.unsplashmodel.authmodel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anh.hoang on 23.11.17.
 */

public class TokenRequest {
    @SerializedName("client_id")
    private String clientId;
    @SerializedName("client_secret")
    private String clientSecret;
    @SerializedName("redirect_uri")
    private String redirectUri;
    @SerializedName("code")
    private String authorizationCode;
    @SerializedName("grant_type")
    private String grantType;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }
}
