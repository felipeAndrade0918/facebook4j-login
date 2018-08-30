package com.batutapps.facebooklogin.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.batutapps.facebooklogin.service.FacebookOauthService;

import facebook4j.Facebook;

@Controller
@RequestMapping("/oauth")
public class FacebookOauthController {

	private FacebookOauthService oauthService;
	
	@Autowired
	public FacebookOauthController(FacebookOauthService oauthService) {
		super();
		this.oauthService = oauthService;
	}

	@RequestMapping("/signin")
	public void signin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Facebook facebook = oauthService.setupFacebookClient();
		
		request.getSession().setAttribute("facebook", facebook);
		
		String callbackUrl = oauthService.getCallbackUrl(request.getRequestURL().toString());
		response.sendRedirect(facebook.getOAuthAuthorizationURL(callbackUrl.toString()));
	}
	
	@RequestMapping("/callback")
	public void callback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Facebook facebook = (Facebook) request.getSession().getAttribute("facebook");
        String oauthCode = request.getParameter("code");
        
        oauthService.setAccessToken(facebook, oauthCode);
        
        response.sendRedirect(request.getContextPath() + "/");
	}
	
	@RequestMapping("/me")
	@ResponseBody
	public String me(HttpServletRequest request, HttpServletResponse response) {
		Facebook facebook = (Facebook) request.getSession().getAttribute("facebook");

		return oauthService.getUserName(facebook);
	}
	
}