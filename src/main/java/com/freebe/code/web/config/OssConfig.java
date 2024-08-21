package com.freebe.code.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * OSS 配置
 * @author xiezhengbiao
 *
 */
@Data
@Component
@ConfigurationProperties(prefix = "oss")
public class OssConfig {
	
	private String endpoint;
	
    private String accessKeyId;
    
    private String accessKeySecret;
    
    private String bucketName;
    
    private String delimiter = "/"; // 目录分割符，默认"/"
    
}
