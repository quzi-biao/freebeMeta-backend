package com.freebe.code.web.filter;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.alibaba.fastjson.JSON;
import com.freebe.code.business.base.service.UserService;
import com.freebe.code.business.base.vo.UserVO;
import com.freebe.code.common.Constants;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ResultBean;
import com.freebe.code.util.JwtUtils;
import com.freebe.code.web.config.SecurityWhitePaths;

public class JWTAuthenticationFilter extends BasicAuthenticationFilter   {
	private SecurityWhitePaths whitePaths;
	
	private UserService userService;
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, 
			SecurityWhitePaths whitePaths, 
			UserService userService) {
        super(authenticationManager);
        this.whitePaths = whitePaths;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        try {
        	if(UrlPatternMatcher.match(request, whitePaths.getWhiteList()) || request.getRequestURI().equals("/")) {
            	chain.doFilter(request, response);
            	return;
            }
        	
			Long userId = getUserId(request);
            // 将组织信息和用户相关的信息写入到上下文中
            fillContextData(request, userId);
            chain.doFilter(request, response);
        }catch (Exception e){
        	e.printStackTrace();
        	response.setCharacterEncoding("UTF-8");
            try {
				response.getWriter().write(JSON.toJSONString(ResultBean.error(e.getMessage())));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        }
    }

	private Long getUserId(HttpServletRequest request) throws CustomException {
		String header = request.getHeader(JwtUtils.AUTHORIZATION);
		if (StringUtils.isBlank(header)) {
			header = request.getParameter("token");
			if (StringUtils.isBlank(header)) {
				System.out.println("用户未登录: " + request.getRequestURI());
				throw new CustomException("您要登录后才能执行此操作");
			}
		}
		UsernamePasswordAuthenticationToken authentication = getAuthentication(header);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		if(null != authentication.getName()) {
			return Long.parseLong(authentication.getName());
		}
		
		return null;
	}

	private void fillContextData(HttpServletRequest request, Long userId)
			throws CustomException {
		if(null == userId) {
			return;
		}
		// 把用户信息填入到请求中
		UserVO user = userService.getUser(userId);
		request.setAttribute(Constants.USER_INFO, user);
	}

    private UsernamePasswordAuthenticationToken getAuthentication(String token) throws CustomException  {
        if (token != null) {
            String userName="";

            try {
                // 解密Token
                userName = JwtUtils.getAudience(token);
                if (StringUtils.isNotBlank(userName)) {
                    return new UsernamePasswordAuthenticationToken(userName, null, new ArrayList<>());
                }
            }catch (Exception e){
            	throw new CustomException("token 校验错误", e);
            }
            return null;
        }
        return null;
    }

}
