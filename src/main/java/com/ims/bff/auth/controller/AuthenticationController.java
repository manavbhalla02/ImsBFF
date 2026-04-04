package com.ims.bff.auth.controller;

import static com.ims.bff.auth.AuthConstants.AUTH_API_BASE_PATH;

import java.net.URI;

import jakarta.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ims.bff.auth.dto.AuthProvidersResponse;
import com.ims.bff.auth.dto.AuthenticationResponse;
import com.ims.bff.auth.dto.BasicLoginRequest;
import com.ims.bff.auth.service.AuthenticationApplicationService;

@RestController
@RequestMapping(AUTH_API_BASE_PATH)
public class AuthenticationController {

    private final AuthenticationApplicationService authenticationService;

    public AuthenticationController(AuthenticationApplicationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/providers")
    public AuthProvidersResponse getProviders() {
        return authenticationService.getSupportedProviders();
    }

    @PostMapping("/login/basic")
    public AuthenticationResponse loginWithBasic(@Valid @RequestBody BasicLoginRequest request) {
        return authenticationService.loginWithBasic(request);
    }

    @GetMapping("/login/{type}/{provider}")
    public ResponseEntity<Void> redirectToExternalLogin(
            @PathVariable String type,
            @PathVariable String provider) {
        String loginPath = authenticationService.resolveExternalLoginPath(type, provider);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, URI.create(loginPath).toString())
                .build();
    }

    @GetMapping("/user")
    public ResponseEntity<AuthenticationResponse> getCurrentUser(Authentication authentication) {
        AuthenticationResponse response = authenticationService.getAuthenticatedUser(authentication);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(response);
    }
}
