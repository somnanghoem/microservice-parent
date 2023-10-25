package org.microservices.securityservice.model;

import lombok.Data;

@Data
public class GenerateUserTokenRequest {
    private String userName;
    private String password;
}
