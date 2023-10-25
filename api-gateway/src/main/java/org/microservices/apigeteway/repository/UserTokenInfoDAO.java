package org.microservices.apigeteway.repository;

import org.apache.ibatis.annotations.Mapper;
import org.microservices.apigeteway.dto.UserTokenInfoDTO;

@Mapper
public interface UserTokenInfoDAO {
    UserTokenInfoDTO retrieveUserTokenInfo(UserTokenInfoDTO param) throws Exception;
    long registerUserTokenInfo (UserTokenInfoDTO param) throws Exception;
    long updateUserTokenInfo(UserTokenInfoDTO param) throws Exception;
    UserTokenInfoDTO retrieveUserTokenInfoByToken( UserTokenInfoDTO param ) throws Exception;
}