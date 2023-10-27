package com.microservices.orderservice.service;

import com.util.responseutil.util.RequestData;
import com.util.responseutil.util.ResponseData;

import java.util.Map;
import java.util.Objects;

public interface APIConnectorService {
    public ResponseData<Map<String, Objects>> postRequest(RequestData requestData, String url ) throws Exception;
    public ResponseData<Map<String, Objects>> getRequest(RequestData requestData, String url ) throws Exception;
}
