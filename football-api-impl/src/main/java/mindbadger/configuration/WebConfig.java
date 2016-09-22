package com.kingfisher.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class WebConfig extends WebMvcConfigurerAdapter{

    private static final String WILDCARD_PATH = "/**";
    private static final String[] HTTP_METHODS = {"GET", "POST", "PATCH", "DELETE", "PUT", "OPTIONS", "HEAD", "TRACE"};

    @Value("${server.allowed.origins}")
    private String allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(WILDCARD_PATH).allowedOrigins(allowedOrigins).allowedMethods(HTTP_METHODS);
    }
}
