package com.batutapps.facebooklogin.service;

import facebook4j.Facebook;

public interface FacebookOauthService {

	Facebook setupFacebookClient();

	String getCallbackUrl(String requestUrl);

	void setAccessToken(Facebook facebook, String oauthCode) throws Exception;

	String getUserName(Facebook facebook);

}