package com.ims.bff.security.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.ims.bff.auth.AuthConstants;
import com.ims.bff.auth.properties.AuthProperties;

@Configuration
@EnableConfigurationProperties(AuthProperties.class)
public class AuthBeansConfiguration {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(AuthProperties authProperties, PasswordEncoder passwordEncoder) {
        AuthProperties.Basic basic = authProperties.getBasic();
        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();

        if (basic.isEnabled()) {
            userDetailsManager.createUser(User.withUsername(basic.getUsername())
                    .password(passwordEncoder.encode(basic.getPassword()))
                    .roles(resolveRole(basic.getRole()))
                    .build());
        }

        return userDetailsManager;
    }

    @Bean
    AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    private String resolveRole(String configuredRole) {
        if (configuredRole == null || configuredRole.isBlank()) {
            return AuthConstants.DEFAULT_BASIC_ROLE;
        }
        return configuredRole.replace("ROLE_", "");
    }
}
