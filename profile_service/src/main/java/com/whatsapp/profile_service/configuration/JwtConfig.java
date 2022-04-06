package com.whatsapp.profile_service.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtConfig {
      private String cookieName;
      private Long timeDelta;
      private String secretKey;
}
