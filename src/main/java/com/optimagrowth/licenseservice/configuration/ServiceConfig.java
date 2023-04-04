package com.optimagrowth.licenseservice.configuration;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "example")
@Getter
public class ServiceConfig {
    private String property;
}
