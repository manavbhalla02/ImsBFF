package com.ims.bff.auth.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.ims.bff.auth.dto.AuthenticationResponse;

@SpringBootTest
class AuthenticationApplicationServiceTest {

    @Autowired
    private AuthenticationApplicationService authenticationService;

    @Test
    void shouldHandleGoogleCallbackThroughGoogleStrategy() {
        OAuth2User googleUser = new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                Map.of(
                        "email", "google.user@example.com",
                        "name", "Google User",
                        "sub", "google-subject"),
                "email");

        OAuth2AuthenticationToken authentication = new OAuth2AuthenticationToken(
                googleUser,
                googleUser.getAuthorities(),
                "google");

        AuthenticationResponse response = authenticationService.handleAuthenticationSuccess(authentication);

        assertThat(response.authenticationType()).isEqualTo("OAUTH2");
        assertThat(response.provider()).isEqualTo("google");
        assertThat(response.username()).isEqualTo("google.user@example.com");
        assertThat(response.message()).isEqualTo("Google OAuth2 callback handled successfully");
    }

    @Test
    void shouldResolveOktaUserThroughSsoStrategy() {
        OAuth2User oktaUser = new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                Map.of(
                        "preferred_username", "okta.user@example.com",
                        "email", "okta.user@example.com",
                        "sub", "okta-subject"),
                "preferred_username");

        OAuth2AuthenticationToken authentication = new OAuth2AuthenticationToken(
                oktaUser,
                oktaUser.getAuthorities(),
                "okta");

        AuthenticationResponse response = authenticationService.getAuthenticatedUser(authentication);

        assertThat(response.authenticationType()).isEqualTo("SSO");
        assertThat(response.provider()).isEqualTo("okta");
        assertThat(response.username()).isEqualTo("okta.user@example.com");
        assertThat(response.message()).isEqualTo("Okta SSO user resolved successfully");
    }

    @Test
    void shouldResolveBasicUserThroughBasicResultStrategy() {
        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(
                "ims-user",
                "n/a",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        AuthenticationResponse response = authenticationService.getAuthenticatedUser(authentication);

        assertThat(response.authenticationType()).isEqualTo("BASIC");
        assertThat(response.provider()).isEqualTo("basic");
        assertThat(response.username()).isEqualTo("ims-user");
        assertThat(response.message()).isEqualTo("Basic user resolved successfully");
    }
}
