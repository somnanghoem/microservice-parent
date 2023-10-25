package org.microservices.securityservice.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GenerateUserTokenResponse {
    private String token = "";
    private String expiredDate = "";
    private String expiredTime = "";
    private String issuedDate = "";
    private String issuedTime = "";
    private List<UserTokenScope> scopes = new ArrayList<>();
}
