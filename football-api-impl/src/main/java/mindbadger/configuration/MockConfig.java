package com.kingfisher.configuration;

import com.kingfisher.api.configuration.ApiMockConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static com.kingfisher.util.JsonReader.getObjectFromJson;

@Configuration
@ComponentScan("com.kingfisher")
public class MockConfig {

    private static final Logger LOG = LoggerFactory.getLogger(MockConfig.class);

    @Value("${config.location}")
    private String configLocation;

    @Bean
    public ApiMockConfiguration apiMockConfiguration() {
        ApiMockConfiguration configuration = getObjectFromJson(configLocation, ApiMockConfiguration.class);
        if (configuration == null) {
            LOG.error("Configuration file either not found or has broken syntax. Check config.location in application.properties");
        }

        return configuration;
    }
}
