package org.microservices.securityservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:config.properties")
public class PropertiesPlaceholderConfiguration {

    @Value("${SHA256.secret}")
    private String sha256Secret;
    public String getSha256Secret() {
        return sha256Secret;
    }
    public void setSha256Secret(String sha256Secret) {
        this.sha256Secret = sha256Secret;
    }
}
