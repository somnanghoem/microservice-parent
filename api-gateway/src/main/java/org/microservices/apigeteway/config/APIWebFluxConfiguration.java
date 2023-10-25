package org.microservices.apigeteway.config;

import org.microservices.apigeteway.service.CustomUserDetailService;
import org.microservices.apigeteway.util.KeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class APIWebFluxConfiguration {

    @Autowired
    KeyUtils keyUtils;
    @Autowired
    CustomUserDetailService customUserDetailService;
    @Autowired
    APIAuthenticationFilter aPIAuthenticationFilter;

    @Bean
    SecurityWebFilterChain apiHttpSecurity(ServerHttpSecurity http) {
        http
                .csrf((csrf) -> csrf.disable())
                .authorizeExchange((exchanges) -> exchanges
                        .pathMatchers("/api/security/v1/token/**").permitAll()
                        .anyExchange().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
        http.addFilterBefore(aPIAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION );
        return http.build();
    }

    @Bean
    ReactiveUserDetailsService userDetailsService() {
        return (username) -> (Mono<UserDetails>) customUserDetailService.loadUserByUsername(username);
    }

    @Bean
    @Primary
    ReactiveJwtDecoder jwtAccessTokenDecoder() {
        return NimbusReactiveJwtDecoder.withPublicKey(keyUtils.getAccessTokenPublicKey()).build();
    }


}
