package com.microservices.orderservice.service;

import com.util.responseutil.util.RequestData;

import java.util.Map;
import java.util.Objects;

public interface APIConnectorService {
    public Map<String, Objects> postRequest( RequestData requestData ) throws Exception;
}
