package com.microservices.orderservice.repository;

import com.microservices.orderservice.dto.UserTokenInfoDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserTokenInfoDAO {
    UserTokenInfoDTO retrieveUserTokenInfo(UserTokenInfoDTO param) throws Exception;
    long registerUserTokenInfo (UserTokenInfoDTO param) throws Exception;
    long updateUserTokenInfo(UserTokenInfoDTO param) throws Exception;
    UserTokenInfoDTO retrieveUserTokenInfoByToken( UserTokenInfoDTO param ) throws Exception;
}