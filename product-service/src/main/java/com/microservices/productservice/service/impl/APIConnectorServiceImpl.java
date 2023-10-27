package com.microservices.productservice.service.impl;

import com.microservices.productservice.service.APIConnectorService;
import com.util.responseutil.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
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
            Mono<ResponseData> responseData  = webClientBuilder.build().post()
                    .uri(url)
                    .bodyValue( requestData )
                    .retrieve()
                    .bodyToMono(ResponseData.class);

           Map<String, Objects> body = (Map<String, Objects>) responseData.map(m->m.getBody()).toFuture().get();
           ResponseHeader header = (ResponseHeader) responseData.map(m->m.getHeader()).toFuture().get();
           ResponseData<Map<String, Objects>> result = new ResponseData<>(header, body);

           return  result;

        }catch ( Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseData<Map<String, Objects>> getRequest( RequestData requestData, String url) throws Exception {
        try {
            /*************************************
             * Call to Other Service By WebClient
             *************************************/
            Mono<ResponseData> responseData  = webClientBuilder.build().get()
                    .uri(url)
                    .accept(MediaType.APPLICATION_JSON).header("Authorization", "{{authtoken}}")
                    .retrieve()
                    .bodyToMono(ResponseData.class);

            Map<String, Objects> body = (Map<String, Objects>) responseData.map(m->m.getBody()).toFuture().get();
            ResponseHeader header = (ResponseHeader) responseData.map(m->m.getHeader()).toFuture().get();
            ResponseData<Map<String, Objects>> result = new ResponseData<>(header, body);

            return  result;
        }catch ( Exception e) {
           throw e;
        }
    }
}
