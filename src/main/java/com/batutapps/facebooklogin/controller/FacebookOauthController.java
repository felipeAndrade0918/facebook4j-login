package com.batutapps.facebooklogin.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.batutapps.facebooklogin.service.FacebookOauthService;

import facebook4j.Facebook;

@Controller
@RequestMapping("/oauth")
public class FacebookOauthController {

	private static final String FACEBOOK_PARAM = "facebook";
	
	private FacebookOauthService oauthService;
	
	@Autowired
	public FacebookOauthController(FacebookOauthService oauthService) {
		super();
		this.oauthService = oauthService;
	}

	@GetMapping("/signin")
	public void signin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Facebook facebook = oauthService.setupFacebookClient();
		
		request.getSession().setAttribute(FACEBOOK_PARAM, facebook);
		
		String callbackUrl = oauthService.getCallbackUrl(request.getRequestURL().toString());
		response.sendRedirect(facebook.getOAuthAuthorizationURL(callbackUrl));
	}
	
	@GetMapping("/callback")
	public void callback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Facebook facebook = (Facebook) request.getSession().getAttribute(FACEBOOK_PARAM);
        String oauthCode = request.getParameter("code");
        
        oauthService.setAccessToken(facebook, oauthCode);
        
        response.sendRedirect(request.getContextPath() + "/");
	}
	
	@GetMapping("/me")
	@ResponseBody
	public String me(HttpServletRequest request, HttpServletResponse response) {
		Facebook facebook = (Facebook) request.getSession().getAttribute(FACEBOOK_PARAM);

		return oauthService.getUserName(facebook);
	}
	
}