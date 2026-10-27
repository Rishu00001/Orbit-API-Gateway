package com.orbit.management.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String tokenType;
    private Long expiresInSecond;
    private String Role;
    private Long organizationId;
}
