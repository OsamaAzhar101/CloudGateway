package com.oasys.CloudGateway.model;

import lombok.*;

import java.util.Collection;

@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class AuthenticationResponse {

    private String userId;
    private String refreshToken;
    private long expiresAt;
    private String accessToken;

    private Collection<String> authorities;

}
