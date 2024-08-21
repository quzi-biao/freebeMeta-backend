package com.freebe.code.web.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class UrlPatternMatcher {

	public static boolean match(HttpServletRequest request, String[] urlPatterns) {
		for(String urlPattern : urlPatterns) {
			AntPathRequestMatcher matcher = new AntPathRequestMatcher(urlPattern);
			if(matcher.matches(request)) {
				return true;
			}
		}
		return false;
	}

}
