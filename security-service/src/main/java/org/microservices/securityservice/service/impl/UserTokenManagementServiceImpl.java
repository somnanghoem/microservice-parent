package org.microservices.securityservice.service.impl;


import com.util.responseutil.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.microservices.securityservice.config.PropertiesPlaceholderConfiguration;
import org.microservices.securityservice.config.TokenUtil;
import org.microservices.securityservice.dto.UserInfoDTO;
import org.microservices.securityservice.dto.UserTokenInfoDTO;
import org.microservices.securityservice.model.GenerateUserTokenRequest;
import org.microservices.securityservice.model.GenerateUserTokenResponse;
import org.microservices.securityservice.repository.UserInfoDAO;
import org.microservices.securityservice.repository.UserTokenInfoDAO;
import org.microservices.securityservice.service.UserTokenManagementService;
import org.microservices.securityservice.type.ResponseResultMessage;
import org.microservices.securityservice.util.Sha256Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class UserTokenManagementServiceImpl implements UserTokenManagementService {
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private UserInfoDAO userInfoDAO;
    @Autowired
    private UserTokenInfoDAO userTokenInfoDAO;
    @Autowired
    private PropertiesPlaceholderConfiguration config;
    @Autowired
    PlatformTransactionManager platformTransactionManager;
    @Override
    public GenerateUserTokenResponse generateUserTokenInfo(GenerateUserTokenRequest requestParam) throws Exception {
        // Open New Transaction
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction( new DefaultTransactionAttribute( TransactionDefinition.PROPAGATION_REQUIRES_NEW ) );
        try {
            // validate request data
            this.validationRequestData(requestParam);
            // Generate Token Info
            GenerateUserTokenResponse tokenResponse =processRegisterUpdateUserTokenInfo(requestParam);
            // Commit Transaction
            platformTransactionManager.commit(transactionStatus);
            return tokenResponse;
        } catch ( Exception e ) {
            // Rollback transaction
            platformTransactionManager.rollback(transactionStatus);
            throw e;
        }
    }

    private void validationRequestData( GenerateUserTokenRequest requestParam ) throws Exception {
        if (StringUtils.isBlank(requestParam.getUserName()) || StringUtils.isEmpty(requestParam.getUserName())) {
            throw new Exception(ResponseResultMessage.USER_NAME_EMPTY.getValue());
        } else if (StringUtils.isBlank(requestParam.getPassword()) || StringUtils.isEmpty(requestParam.getPassword())) {
            throw new Exception(ResponseResultMessage.PASSWORD_EMPTY.getValue());
        }
        // Validate User Information
        UserInfoDTO userParam = new UserInfoDTO();
        userParam.setUserName(requestParam.getUserName());
        UserInfoDTO userInfo = userInfoDAO.retrieveUserInfo(userParam);
        if (userInfo == null) {
            throw new Exception(ResponseResultMessage.USER_NOT_FOUND.getValue());
        } else {
            String encryptUserPassword = Sha256Util.encrypt(requestParam.getPassword(), config.getSha256Secret().concat(requestParam.getUserName()));
            if (!encryptUserPassword.equals(userInfo.getUserPassword())) {
                throw new Exception(ResponseResultMessage.INVALID_PASSWORD.getValue());
            }
        }
    }

    private GenerateUserTokenResponse processRegisterUpdateUserTokenInfo( GenerateUserTokenRequest requestParam ) throws Exception {

        try {
            GenerateUserTokenResponse tokenResponse = new GenerateUserTokenResponse();
            UserTokenInfoDTO registerUserInfo = new UserTokenInfoDTO();
            // Retrieve User Token Info
            UserTokenInfoDTO userParam = new UserTokenInfoDTO();
            userParam.setUserName(requestParam.getUserName());
            UserTokenInfoDTO userTokenInfo = userTokenInfoDAO.retrieveUserTokenInfo(userParam);

            // Process Register User Token Info
            if ( userTokenInfo == null ) {
                // Generate Token Info
                tokenResponse = tokenUtil.generateAccessToken(requestParam);
                registerUserInfo.setUserName( requestParam.getUserName() );
                registerUserInfo.setToken( tokenResponse.getToken() );
                registerUserInfo.setIssuedDate( tokenResponse.getIssuedDate() );
                registerUserInfo.setIssuedTime( tokenResponse.getIssuedTime() );
                registerUserInfo.setExpiredDate( tokenResponse.getExpiredDate() );
                registerUserInfo.setExpiredTime( tokenResponse.getExpiredTime() );
                registerUserInfo.setRemoteIP("");
                registerUserInfo.setStatus("0"); // 0: Normal, 1: Delete
                registerUserInfo.setUserType("01"); // 01: Mobile, 02: Web
                userTokenInfoDAO.registerUserTokenInfo(registerUserInfo);
            }
            // Process Update User Token Info
            else {
                SimpleDateFormat sdDate = new SimpleDateFormat(DateUtil.DATETIME);
                Date expiredDateTime = sdDate.parse( userTokenInfo.getExpiredDate().concat(userTokenInfo.getExpiredTime()) ) ;
                Date currentDateTime = sdDate.parse(DateUtil.getCurrentFormatDate(DateUtil.DATETIME));
                if (( expiredDateTime.compareTo(currentDateTime) <=0)) {
                    tokenResponse = tokenUtil.generateAccessToken(requestParam);
                    registerUserInfo = userTokenInfo;
                    registerUserInfo.setUserName( requestParam.getUserName() );
                    registerUserInfo.setToken( tokenResponse.getToken() );
                    registerUserInfo.setIssuedDate( tokenResponse.getIssuedDate() );
                    registerUserInfo.setIssuedTime( tokenResponse.getIssuedTime() );
                    registerUserInfo.setExpiredDate( tokenResponse.getExpiredDate() );
                    registerUserInfo.setExpiredTime( tokenResponse.getExpiredTime() );
                    userTokenInfoDAO.updateUserTokenInfo(registerUserInfo);
                } else {
                    tokenResponse.setToken(userTokenInfo.getToken());
                    tokenResponse.setIssuedDate(userTokenInfo.getIssuedDate());
                    tokenResponse.setIssuedTime(userTokenInfo.getIssuedTime());
                    tokenResponse.setExpiredDate(userTokenInfo.getExpiredDate());
                    tokenResponse.setExpiredTime(userTokenInfo.getExpiredTime());
                }
            }
            return  tokenResponse;
        } catch ( Exception e ) {
            e.printStackTrace();
            throw new Exception( ResponseResultMessage.REGISTER_TOKEN_ERROR.getValue() );
        }
    }
}
