package com.batutapps.facebooklogin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;

@Service
public class FacebookOauthServiceImpl implements FacebookOauthService {

	public static final Logger logger = LoggerFactory.getLogger(FacebookOauthServiceImpl.class);
	
	@Value("${facebook.oauth.app.id}")
	private String appId;
	
	@Value("${facebook.oauth.app.secret}")
	private String appSecret;
	
	@Override
	public Facebook setupFacebookClient() {
		Facebook facebook = new FacebookFactory().getInstance();
		facebook.setOAuthAppId(appId, appSecret);
		
		return facebook;
	}
	
	@Override
	public String getCallbackUrl(String requestUrl) {
		StringBuilder callbackUrl = new StringBuilder(requestUrl);
		
        int index = callbackUrl.lastIndexOf("/");
        callbackUrl.replace(index, callbackUrl.length(), "").append("/callback");
        
        return callbackUrl.toString();
	}
	
	@Override
	public void setAccessToken(Facebook facebook, String oauthCode) throws Exception {
        if (oauthCode != null) {
        	try {
        		AccessToken token = facebook.getOAuthAccessToken(oauthCode);
        		facebook.setOAuthAccessToken(token);
        	} catch (FacebookException e) {
        		logger.error("Error while setting the access token", e);
        	}
        }
	}
	
	@Override
	public String getUserName(Facebook facebook) {
		if (facebook != null) {
			try {
				return facebook.getMe().getName();
			} catch (IllegalStateException e) {
				logger.error("User is not logged in", e);
			} catch (FacebookException e) {
				logger.error("An error occurred while fetching the user's name", e);
			}
		}
		
		return null;
	}
}