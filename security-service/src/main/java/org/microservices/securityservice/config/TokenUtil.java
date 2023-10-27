package org.microservices.securityservice.config;

import com.util.responseutil.util.DateUtil;
import org.microservices.securityservice.model.GenerateUserTokenRequest;
import org.microservices.securityservice.model.GenerateUserTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class TokenUtil {
    @Autowired
    JwtEncoder accessTokenEncoder;

    @Autowired
    @Qualifier("jwtRefreshTokenEncoder")
    JwtEncoder refreshTokenEncoder;

    public GenerateUserTokenResponse generateAccessToken(GenerateUserTokenRequest generateUserTokenRequst)  {

        Instant now = Instant.now();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer(generateUserTokenRequst.getUserName())
                .issuedAt(now)
                .expiresAt(now.plus(5, ChronoUnit.MINUTES ) )
                .subject(generateUserTokenRequst.getUserName())
                .build();
        Date expired =  Date.from(accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getExpiresAt());
        Date issued = Date.from(accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getIssuedAt());
        String expiredDate = DateUtil.getDateTimeByFormat(expired,"yyyyMMdd");
        String expiredTime = DateUtil.getDateTimeByFormat(expired,"HHmmss");
        String IssuedDate = DateUtil.getDateTimeByFormat(issued,"yyyyMMdd");
        String issuedTime = DateUtil.getDateTimeByFormat(issued,"HHmmss");
        GenerateUserTokenResponse userTokenInfo = new GenerateUserTokenResponse();
        userTokenInfo.setToken( accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue() );
        userTokenInfo.setExpiredDate(expiredDate);
        userTokenInfo.setExpiredTime(expiredTime);
        userTokenInfo.setIssuedDate(IssuedDate);
        userTokenInfo.setIssuedTime(issuedTime);
        return userTokenInfo ;
    }

    public String generateRefreshToken( GenerateUserTokenRequest generateUserTokenRequst) {
        Instant now = Instant.now();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer( generateUserTokenRequst.getUserName() )
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.MINUTES ) )
                .subject( generateUserTokenRequst.getUserName() )
                .build();
        return  accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }
}
