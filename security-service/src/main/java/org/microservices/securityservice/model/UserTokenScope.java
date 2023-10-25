package org.microservices.securityservice.model;

import lombok.Data;

@Data
public class UserTokenScope {
    private String scropeID = "";
    private String scropeName = "";
}
