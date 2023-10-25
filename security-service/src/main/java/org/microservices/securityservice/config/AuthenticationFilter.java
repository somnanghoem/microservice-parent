package org.microservices.securityservice.config;

import com.util.responseutil.util.DateUtil;
import com.util.responseutil.util.RenderUtil;
import com.util.responseutil.util.ResponseData;
import com.util.responseutil.util.ResponseHeader;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.microservices.securityservice.dto.UserTokenInfoDTO;
import org.microservices.securityservice.repository.UserTokenInfoDAO;
import org.microservices.securityservice.service.CustomUserDetailService;
import org.microservices.securityservice.type.ResponseResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserTokenInfoDAO userTokenInfoDAO;
    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");
        System.out.println(">>>Security Service Filter>>>");
        String userName = "";
        String token = "";
        String requestURI = request.getRequestURI();
        if ( requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ") ) {
            try {
                token = requestTokenHeader.substring(7);
                // Validate User Token Info
                UserTokenInfoDTO userTokenParam = new UserTokenInfoDTO();
                userTokenParam.setToken(token);
                UserTokenInfoDTO userTokenInfo = userTokenInfoDAO.retrieveUserTokenInfoByToken(userTokenParam);
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
                RenderUtil.renderJson( response, new ResponseData<>( header, new Object() ) );
                return;
            }
        }
        // In case authentication request
        if ( StringUtils.isNoneEmpty( userName ) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.customUserDetailService.loadUserByUsername( userName );
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            /********************************************************************************
             * Do not use SecurityContextHolder.getContext().setAuthentication(authentication)
             * to avoid race conditions across multiple threads
             *********************************************************************************/
        }
        filterChain.doFilter(request, response);
    }
}
