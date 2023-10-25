package org.microservices.securityservice.config;

import org.microservices.securityservice.dto.CustomUserDetail;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class JwtUserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {
    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt jwt) {

        CustomUserDetail user = new CustomUserDetail();
        user.setId( jwt.getSubject() );
        return new UsernamePasswordAuthenticationToken(user, jwt, Collections.EMPTY_LIST );
    }
}
