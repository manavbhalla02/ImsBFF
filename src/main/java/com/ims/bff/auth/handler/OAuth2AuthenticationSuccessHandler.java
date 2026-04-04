package com.ims.bff.auth.handler;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ims.bff.auth.dto.AuthenticationResponse;
import com.ims.bff.auth.service.AuthenticationApplicationService;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthenticationApplicationService authenticationService;
    private final ObjectMapper objectMapper;

    public OAuth2AuthenticationSuccessHandler(
            AuthenticationApplicationService authenticationService,
            ObjectMapper objectMapper) {
        this.authenticationService = authenticationService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        AuthenticationResponse authenticationResponse = authenticationService.handleAuthenticationSuccess(authentication);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), authenticationResponse);
    }
}
