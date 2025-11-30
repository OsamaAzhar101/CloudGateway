package com.oasys.CloudGateway.controller;

import com.oasys.CloudGateway.model.AuthenticationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/authenticate")
public class AuthenticationController {


    private final ReactiveClientRegistrationRepository clientRegistrationRepository;

    private final ReactiveOAuth2AuthorizedClientService authorizedClientService;

    public AuthenticationController(ReactiveOAuth2AuthorizedClientService authorizedClientService, ReactiveClientRegistrationRepository clientRegistrationRepository) {
        this.authorizedClientService = authorizedClientService;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

/*    @GetMapping("/login")
    public ResponseEntity<?> authenticate(
            @AuthenticationPrincipal
            OidcUser oidcUser,
            Model model,
            @RegisteredOAuth2AuthorizedClient("okta")
            OAuth2AuthorizedClient authorizedClient
    ) {

        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .userId(oidcUser.getEmail())
                .accessToken(authorizedClient.getAccessToken().getTokenValue())
                .refreshToken(authorizedClient.getRefreshToken().getTokenValue())
                .expiresAt(authorizedClient.getAccessToken().getExpiresAt().getEpochSecond())
                .authorities(oidcUser.getAuthorities()
                        .stream().map(grantedAuthority -> grantedAuthority.getAuthority()).toList())
                .build();
        return new ResponseEntity<>(authenticationResponse, org.springframework.http.HttpStatus.OK);
    }*/

/*    @GetMapping("/login")
    public Mono<ResponseEntity<AuthenticationResponse>> authenticate(
            @AuthenticationPrincipal Mono<OidcUser> oidcUserMono,
            ServerWebExchange exchange
    ) {
        return oidcUserMono.flatMap(oidcUser ->
                exchange.getSession().flatMap(session -> {
                    // Get OAuth2 authorized client from session
                    OAuth2AuthorizedClient authorizedClient =
                            (OAuth2AuthorizedClient) session.getAttribute("SPRING_SECURITY_OAUTH2_AUTHORIZED_CLIENT_okta");

                    if (authorizedClient == null) {
                        return Mono.error(new IllegalStateException("No authorized client found"));
                    }

                    AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                            .userId(oidcUser.getEmail())
                            .accessToken(authorizedClient.getAccessToken().getTokenValue())
                            .refreshToken(authorizedClient.getRefreshToken() != null
                                    ? authorizedClient.getRefreshToken().getTokenValue()
                                    : null)
                            .expiresAt(authorizedClient.getAccessToken().getExpiresAt().getEpochSecond())
                            .authorities(oidcUser.getAuthorities()
                                    .stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .toList())
                            .build();

                    return Mono.just(ResponseEntity.ok(authenticationResponse));
                })
        );
    }*/



    @GetMapping("/login")
    public Mono<Void> login(ServerWebExchange exchange) {
        // Redirect to OAuth2 authorization endpoint
        return Mono.fromRunnable(() ->
                exchange.getResponse().setStatusCode(HttpStatus.FOUND)
        ).then(Mono.fromRunnable(() ->
                exchange.getResponse().getHeaders().setLocation(
                        URI.create("/oauth2/authorization/okta")
                )
        )).then();
    }



    @GetMapping("/callback")
    public Mono<ResponseEntity<AuthenticationResponse>> callback(
            @AuthenticationPrincipal Mono<OidcUser> oidcUserMono,
            Authentication authentication
    ) {
        return oidcUserMono.flatMap(oidcUser -> {
            String principalName = authentication.getName();

            return authorizedClientService
                    .loadAuthorizedClient("okta", principalName)
                    .flatMap(authorizedClient -> {
                        if (authorizedClient == null) {
                            return Mono.error(new IllegalStateException("No authorized client found"));
                        }

                        AuthenticationResponse response = AuthenticationResponse.builder()
                                .userId(oidcUser.getEmail())
                                .accessToken(authorizedClient.getAccessToken().getTokenValue())
                                .refreshToken(authorizedClient.getRefreshToken() != null
                                        ? authorizedClient.getRefreshToken().getTokenValue()
                                        : null)
                                .expiresAt(authorizedClient.getAccessToken().getExpiresAt().getEpochSecond())
                                .authorities(oidcUser.getAuthorities()
                                        .stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .toList())
                                .build();

                        return Mono.just(ResponseEntity.ok(response));
                    });
        });
    }

/*
//    To validate from an API
@PostMapping("/token")
public Mono<ResponseEntity<AuthenticationResponse>> getToken(@RequestBody LoginRequest request) {
    // Use Okta Authentication API
    WebClient webClient = WebClient.create("https://dev-123456.okta.com");

    return webClient.post()
            .uri("/oauth2/default/v1/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue("grant_type=password" +
                    "&username=" + request.getEmail() +
                    "&password=" + request.getPassword() +
                    "&scope=openid profile email" +
                    "&client_id=" + clientId +
                    "&client_secret=" + clientSecret)
            .retrieve()
            .bodyToMono(OktaTokenResponse.class)
            .map(tokenResponse -> {
                AuthenticationResponse response = AuthenticationResponse.builder()
                        .userId(request.getEmail())
                        .accessToken(tokenResponse.getAccessToken())
                        .refreshToken(tokenResponse.getRefreshToken())
                        .expiresAt(System.currentTimeMillis() / 1000 + tokenResponse.getExpiresIn())
                        .build();
                return ResponseEntity.ok(response);
            });
}*/

}
