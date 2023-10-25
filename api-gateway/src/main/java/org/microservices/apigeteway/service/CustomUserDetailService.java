package org.microservices.apigeteway.service;

import org.microservices.apigeteway.dto.CustomUserDetail;
import org.microservices.apigeteway.dto.UserInfoDTO;
import org.microservices.apigeteway.repository.UserInfoDAO;
import org.microservices.apigeteway.type.ResponseResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    protected UserInfoDAO userInfoDAO;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserInfoDTO userParam = new UserInfoDTO();
            userParam.setUserName(username);
            UserInfoDTO userInfo = userInfoDAO.retrieveUserInfo(userParam);
            if ( userInfo == null ) {
                throw new UsernameNotFoundException(ResponseResultMessage.USER_NOT_FOUND.getValue());
            }
            return new CustomUserDetail( userInfo.getUserName(), userInfo.getUserPassword());
        } catch (Exception e ) {
            throw new UsernameNotFoundException(ResponseResultMessage.USER_NOT_FOUND.getValue());
        }
    }
}
