 

package com.dhenton9000.spring.mvc.controllers;

 
public class SessionStateHolder {
    
    private String state;
    private String idToken;
    private String accessToken;
    private String refreshToken;
    private int expiresSeconds;
    private String pKCEverifier;

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the idToken
     */
    public String getIdToken() {
        return idToken;
    }

    /**
     * @param idToken the idToken to set
     */
    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    /**
     * @return the accessToken
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * @param accessToken the accessToken to set
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * @return the refreshToken
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * @param refreshToken the refreshToken to set
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * @return the expiresSeconds
     */
    public int getExpiresSeconds() {
        return expiresSeconds;
    }

    /**
     * @param expiresSeconds the expiresSeconds to set
     */
    public void setExpiresSeconds(int expiresSeconds) {
        this.expiresSeconds = expiresSeconds;
    }

    
    public String getAccessData() {
        
        String[] tokenData = this.getAccessToken().split(".");
        
       // return Base64.decode(this.getAccessToken());
       return "fred";
    }

    /**
     * @return the pKCEverifier
     */
    public String getpKCEverifier() {
        return pKCEverifier;
    }

    /**
     * @param pKCEverifier the pKCEverifier to set
     */
    public void setpKCEverifier(String pKCEverifier) {
        this.pKCEverifier = pKCEverifier;
    }
    
    

}
