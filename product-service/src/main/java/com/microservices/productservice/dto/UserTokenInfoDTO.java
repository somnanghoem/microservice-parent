package com.microservices.productservice.dto;

import lombok.Data;

@Data
public class UserTokenInfoDTO {
    private String userName= "";
    private String token= "";
    private String issuedDate= "";
    private String issuedTime= "";
    private String expiredDate= "";
    private String expiredTime= "";
    private String remoteIP= "";
    private String status= "";
    private String userType;
}
