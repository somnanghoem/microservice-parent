package org.microservices.securityservice.repository;

import org.apache.ibatis.annotations.Mapper;
import org.microservices.securityservice.dto.UserInfoDTO;

import java.util.List;

@Mapper
public interface UserInfoDAO {

    long registerUserInfo(UserInfoDTO param ) throws Exception;
    long updateUserInfo(UserInfoDTO param ) throws Exception;
    UserInfoDTO retrieveUserInfo( UserInfoDTO param ) throws  Exception;
    UserInfoDTO retrieveUserInfoByUserNameForUpdate( UserInfoDTO param ) throws Exception;
}
