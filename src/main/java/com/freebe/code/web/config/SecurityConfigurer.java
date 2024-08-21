package com.freebe.code.web.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.freebe.code.business.base.service.UserService;
import com.freebe.code.web.filter.JWTAuthenticationFilter;

/**
 * 
 * @author xiezhengbiao
 *
 */
@Configuration
public class SecurityConfigurer  extends WebSecurityConfigurerAdapter {
	
	@Resource
	private SecurityWhitePaths whitePaths;
	
	@Resource
	private UserService userService;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable() //禁用跨站请求伪造功能
            .authorizeRequests()
            .antMatchers(whitePaths.getWhiteList()).permitAll()
            .antMatchers("/**").authenticated().and().cors()
            .and()
            .addFilter(new JWTAuthenticationFilter(authenticationManager(), whitePaths, userService));
    }
}
