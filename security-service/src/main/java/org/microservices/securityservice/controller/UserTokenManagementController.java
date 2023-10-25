package org.microservices.securityservice.controller;

import com.util.responseutil.util.RequestData;
import com.util.responseutil.util.ResponseData;
import com.util.responseutil.util.ResponseHeader;
import org.microservices.securityservice.model.GenerateUserTokenRequest;
import org.microservices.securityservice.model.GenerateUserTokenResponse;
import org.microservices.securityservice.service.UserTokenManagementService;
import org.microservices.securityservice.type.ResponseResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/security/v1/token")
public class UserTokenManagementController {

    @Autowired
    UserTokenManagementService userTokenManagementService;
    @PostMapping(value = "")
    public ResponseEntity generateUserToken(@RequestBody RequestData<GenerateUserTokenRequest> requestData ) {

        ResponseHeader header = new ResponseHeader("Y", ResponseResultMessage.SUCCESS.getValue(), ResponseResultMessage.SUCCESS.getDescription() );
        GenerateUserTokenResponse body = new GenerateUserTokenResponse();
        try {
            GenerateUserTokenRequest generateUserTokenRequest = requestData.getBody();
            body = userTokenManagementService.generateUserTokenInfo(generateUserTokenRequest);
        } catch ( Exception e ) {
            e.printStackTrace();
            body = new GenerateUserTokenResponse();
            header = ResponseResultMessage.resultOutputMessage(e);
        }
        ResponseData<GenerateUserTokenResponse> responseData = new ResponseData<>(header, body);
        return  ResponseEntity.ok( responseData );
    }
}
