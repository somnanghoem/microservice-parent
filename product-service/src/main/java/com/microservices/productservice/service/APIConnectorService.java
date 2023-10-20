package com.microservices.productservice.service;

import com.util.responseUtil.util.RequestData;
import com.util.responseUtil.util.ResponseData;

import java.util.Map;
import java.util.Objects;

public interface APIConnectorService {
    public ResponseData<Map<String, Objects>> postRequest(RequestData requestData, String url ) throws Exception;
}
