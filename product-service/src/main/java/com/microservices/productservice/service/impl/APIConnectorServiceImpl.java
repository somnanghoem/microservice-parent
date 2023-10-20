package com.microservices.productservice.service.impl;

import com.microservices.productservice.service.APIConnectorService;
import com.util.responseUtil.util.DataUtil;
import com.util.responseUtil.util.RequestData;
import com.util.responseUtil.util.RequestHeader;
import com.util.responseUtil.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Objects;

@Service
public class APIConnectorServiceImpl implements APIConnectorService {

    @Autowired
    WebClient webClient;

    @Override
    public ResponseData<Map<String, Objects>> postRequest(RequestData requestData, String url ) throws Exception {
        try {

            RequestHeader requestHeader = new RequestHeader("somnang","01", "en");
            /***********************************
             * Call to Order Service By WebClient
             *************************************/
            ResponseData result  = webClient.post()
                    .uri(url)
                    .accept(MediaType.APPLICATION_JSON).header("Authorization", "{{authtoken}}")
                    .bodyValue( requestData )
                    .retrieve()
                    .bodyToMono(ResponseData.class)
                    .block();
           return  result;
        }catch ( Exception e) {
            // TODO
        }
        return  new ResponseData<Map<String, Objects>>();
    }
}
