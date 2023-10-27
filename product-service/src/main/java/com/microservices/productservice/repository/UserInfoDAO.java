package com.microservices.productservice.repository;

import com.microservices.productservice.dto.UserInfoDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoDAO {

    long registerUserInfo(UserInfoDTO param ) throws Exception;
    long updateUserInfo(UserInfoDTO param ) throws Exception;
    UserInfoDTO retrieveUserInfo( UserInfoDTO param ) throws  Exception;
    UserInfoDTO retrieveUserInfoByUserNameForUpdate( UserInfoDTO param ) throws Exception;
}
