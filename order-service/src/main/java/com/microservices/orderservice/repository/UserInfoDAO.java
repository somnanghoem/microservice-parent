package com.microservices.orderservice.repository;

import com.microservices.orderservice.dto.UserInfoDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoDAO {

    long registerUserInfo(UserInfoDTO param ) throws Exception;
    long updateUserInfo(UserInfoDTO param ) throws Exception;
    UserInfoDTO retrieveUserInfo( UserInfoDTO param ) throws  Exception;
    UserInfoDTO retrieveUserInfoByUserNameForUpdate( UserInfoDTO param ) throws Exception;
}
