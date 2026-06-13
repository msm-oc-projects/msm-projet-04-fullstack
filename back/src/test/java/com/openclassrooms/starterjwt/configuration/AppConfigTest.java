package com.openclassrooms.starterjwt.configuration;

import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AppConfigTest {

    @Test
    void createsPropertyConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer =
                AppConfig.propertySourcesPlaceholderConfigurer();

        assertNotNull(configurer);
    }

    @Test
    void createsAuthenticationEntryPointComponent() {
        assertNotNull(new AuthEntryPointJwt());
    }
}
