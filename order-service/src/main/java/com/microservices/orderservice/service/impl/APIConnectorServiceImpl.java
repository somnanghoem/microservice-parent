package com.microservices.orderservice.service.impl;

import com.microservices.orderservice.service.APIConnectorService;
import com.util.responseUtil.util.RequestData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Objects;

public class APIConnectorServiceImpl implements APIConnectorService {


    @Override
    public Map<String, Objects> postRequest(RequestData requestData) throws Exception {
        try {


        } catch ( Exception e ) {
            // TODO
        }
        return null;
    }
}
