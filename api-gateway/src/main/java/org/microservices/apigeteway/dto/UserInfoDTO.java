package org.microservices.apigeteway.dto;

import lombok.Data;

@Data
public class UserInfoDTO {

    private String userName= "";
    private String userType= "";
    private String userStatus= "";
    private String userPassword= "";
    private String passwordChangeDate= "";
    private String passwordChangeTime= "";
    private String firstLoginDate= "";
    private String firstLoginTime= "";
    private String lastLoginDate= "";
    private String lastLoginTime= "";
    private String masterUserName= "";
    private String employeeID= "";
    private String createBy= "";
    private String createDate= "";
    private String CreateTime= "";
    private String updateDate= "";
    private String updateTime= "";
    private String updateBy ="";
}
