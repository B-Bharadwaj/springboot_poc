package com.example.order_poc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// Typed config: holds every "app.*" setting from application.yml.
// Binds keys under "app" to the record's fields at startup.
@ConfigurationProperties(prefix = "app")
public record AppProperties(String greeting) {
}
