package org.microservices.securityservice.service;

import org.microservices.securityservice.model.GenerateUserTokenRequest;
import org.microservices.securityservice.model.GenerateUserTokenResponse;

public interface UserTokenManagementService {
     GenerateUserTokenResponse generateUserTokenInfo(GenerateUserTokenRequest requestParam ) throws Exception;

}
