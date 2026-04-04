package com.ims.bff.auth.dto;

import java.util.List;

public record AuthProvidersResponse(List<AuthProviderResponse> providers) {
}
