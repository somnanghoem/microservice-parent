package org.microservices.apigeteway.config;

import com.google.gson.Gson;
import com.util.responseutil.util.DateUtil;
import com.util.responseutil.util.ResponseData;
import com.util.responseutil.util.ResponseHeader;
import org.apache.commons.lang3.StringUtils;
import org.microservices.apigeteway.dto.UserTokenInfoDTO;
import org.microservices.apigeteway.repository.UserTokenInfoDAO;
import org.microservices.apigeteway.service.CustomUserDetailService;
import org.microservices.apigeteway.type.ResponseResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class APIAuthenticationFilter implements WebFilter {
    @Autowired
    UserTokenInfoDAO userTokenInfoDAO;
    @Autowired
    CustomUserDetailService customUserDetailService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        String requestTokenHeader = request.getHeaders().toSingleValueMap().get("authorization");
        String userName = "";
        String token = "";
        String requestURL = String.valueOf(request.getURI());
        System.out.println(">>>API Filter Start>>>");
        // Validate Authorization header
        if ( !requestURL.contains("/api/security/v1/token") && ( requestTokenHeader == null || requestTokenHeader.isEmpty()) ) {

            ResponseHeader header = new ResponseHeader();
            header.setSuccessYN("N");
            header.setResultCode( ResponseResultMessage.MISSING_AUTHORIZATION.getValue() );
            header.setResultMessage( ResponseResultMessage.MISSING_AUTHORIZATION.getDescription() );
            String json = (new Gson()).toJson( new ResponseData<>( header, new Object() ));
            exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(200));
            exchange.getResponse().getHeaders().add("Content-Type", "application/json");
            ByteBuffer byteBuffer = ByteBuffer.wrap(json.getBytes(StandardCharsets.UTF_8));
            DataBuffer dataBuffer = new DefaultDataBufferFactory().wrap(byteBuffer);
            return exchange.getResponse().writeWith(Flux.just(dataBuffer));
        }
        // Validate Token Info
        if ( requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ") ) {
            try {
                token = requestTokenHeader.substring(7);
                // Validate User Token Info
                UserTokenInfoDTO userTokenParam = new UserTokenInfoDTO();
                userTokenParam.setToken(token);
                UserTokenInfoDTO userTokenInfo = userTokenInfoDAO.retrieveUserTokenInfoByToken(userTokenParam);
                System.out.println(">>>API userTokenInfo>>>" + userTokenInfo.toString());
                if ( userTokenInfo == null ) {
                    throw new Exception( ResponseResultMessage.TOKEN_NOT_FOUND.getValue());
                } else {
                    userName = userTokenInfo.getUserName();
                    SimpleDateFormat sdDate = new SimpleDateFormat(DateUtil.DATETIME);
                    Date expiredDateTime = sdDate.parse( userTokenInfo.getExpiredDate().concat(userTokenInfo.getExpiredTime()) ) ;
                    Date currentDateTime = sdDate.parse(DateUtil.getCurrentFormatDate(DateUtil.DATETIME));
                    if (( expiredDateTime.compareTo(currentDateTime) <=0)) {
                        throw new Exception( ResponseResultMessage.TOKEN_EXPIRED.getValue());
                    }
                }
            } catch ( Exception e ) {
                ResponseHeader header = ResponseResultMessage.resultOutputMessage(e);
                String json = (new Gson()).toJson( new ResponseData<>( header, new Object() ));
                exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(200));
                exchange.getResponse().getHeaders().add("Content-Type", "application/json");
                ByteBuffer byteBuffer = ByteBuffer.wrap(json.getBytes(StandardCharsets.UTF_8));
                DataBuffer dataBuffer = new DefaultDataBufferFactory().wrap(byteBuffer);
                return exchange.getResponse().writeWith(Flux.just(dataBuffer));
            }
        }

        // In case authentication request
        if ( StringUtils.isNoneEmpty( userName ) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.customUserDetailService.loadUserByUsername( userName );
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            System.out.println(">>>API Authentication>>>");
            /********************************************************************************
             * Do not use SecurityContextHolder.getContext().setAuthentication(authentication)
             * to avoid race conditions across multiple threads
             *********************************************************************************/
        }
        System.out.println(">>>API Filter END>>>");
        return chain.filter(exchange);
    }

}
