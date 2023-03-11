package dev.qrowned.license.server;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
@Configuration
@ConfigurationProperties(prefix = "license")
public class LicenseServerConfig {

    private String[] apiKeys;
    private List<String> ipWhitelist;

    public LicenseServerConfig() {
        this.apiKeys = new String[]{UUID.randomUUID().toString()};
        this.ipWhitelist = Collections.singletonList("127.0.0.1");
    }

}
