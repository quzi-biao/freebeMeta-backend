package com.freebe.code.web.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig   {  
	
    @Bean
    public Docket createRestApi() {
    	
    	
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .enable(true)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com"))
            .paths(PathSelectors.any())
            .build()
            .securityContexts(Arrays.asList(securityContexts()))
            .securitySchemes(Arrays.asList(securitySchemes(), organizationSchemes()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("二供运维系统接口")
            .version("1.0")
            .build();
    }
    
    private SecurityScheme securitySchemes() {
        return new ApiKey("Authorization", "Authorization", "header");
    }
    
    private SecurityScheme organizationSchemes() {
        return new ApiKey("Organization", "Organization", "header");
    }

    @SuppressWarnings("deprecation")
	private SecurityContext securityContexts() {
        return SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.any())
                        .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("COMMON", "描述信息");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }
    
}
