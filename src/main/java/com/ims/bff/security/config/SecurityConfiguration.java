package com.ims.bff.security.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

import com.ims.bff.auth.AuthConstants;
import com.ims.bff.auth.handler.OAuth2AuthenticationSuccessHandler;

@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            ObjectProvider<ClientRegistrationRepository> clientRegistrationRepositoryProvider,
            OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler) throws Exception {
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/index.html", "/static/**", "/favicon.ico", "/error").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, AuthConstants.AUTH_PROVIDERS_PATH).permitAll()
                        .requestMatchers(HttpMethod.POST, AuthConstants.AUTH_BASIC_LOGIN_PATH).permitAll()
                        .requestMatchers(HttpMethod.GET, AuthConstants.AUTH_API_BASE_PATH + "/login/**").permitAll()
                        .requestMatchers(HttpMethod.GET, AuthConstants.AUTH_USER_PATH).permitAll() // Temporary for debugging
                        .anyRequest().authenticated())
                .httpBasic(withDefaults())
                .formLogin(withDefaults());

        if (clientRegistrationRepositoryProvider.getIfAvailable() != null) {
            http.oauth2Login(oauth2 -> oauth2
                    .successHandler(oauth2AuthenticationSuccessHandler)
                    .failureUrl("/login?error=true"));
        }

        return http.build();
    }
}
