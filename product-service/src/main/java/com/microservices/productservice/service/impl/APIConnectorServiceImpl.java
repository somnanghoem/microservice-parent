package com.microservices.productservice.service.impl;

import com.microservices.productservice.service.APIConnectorService;
import com.util.responseutil.util.RequestData;
import com.util.responseutil.util.RequestHeader;
import com.util.responseutil.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Objects;

@Service
public class APIConnectorServiceImpl implements APIConnectorService {

    @Autowired
    WebClient.Builder webClientBuilder;

    @Override
    public ResponseData<Map<String, Objects>> postRequest(RequestData requestData, String url ) throws Exception {
        try {
            /*************************************
             * Call to Other Service By WebClient
             *************************************/
            ResponseData result  = webClientBuilder.build().post()
                    .uri(url)
                    .accept(MediaType.APPLICATION_JSON).header("Authorization", "{{authtoken}}")
                    .bodyValue( requestData )
                    .retrieve()
                    .bodyToMono(ResponseData.class)
                    .block();
           return  result;
        }catch ( Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseData<Map<String, Objects>> getRequest(RequestData requestData, String url) throws Exception {
        try {
            /*************************************
             * Call to Other Service By WebClient
             *************************************/
            ResponseData result  = webClientBuilder.build().get()
                    .uri(url)
                    .accept(MediaType.APPLICATION_JSON).header("Authorization", "{{authtoken}}")
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
