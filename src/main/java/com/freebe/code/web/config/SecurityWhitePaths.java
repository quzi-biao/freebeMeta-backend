package com.freebe.code.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "security")
public class SecurityWhitePaths {
	private String[] whiteList;
}
