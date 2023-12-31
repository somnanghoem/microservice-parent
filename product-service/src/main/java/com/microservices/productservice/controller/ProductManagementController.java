package com.microservices.productservice.controller;


import com.microservices.productservice.config.PropertiesPlaceholderConfiguration;
import com.microservices.productservice.dto.ProductDTO;
import com.microservices.productservice.model.ProductListResponse;
import com.microservices.productservice.service.APIConnectorService;
import com.microservices.productservice.service.ProductManagementService;
import com.util.responseutil.service.ResponseResultMessageService;
import com.util.responseutil.util.*;
import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/product/v1")
public class ProductManagementController {

    @Autowired
    APIConnectorService aPIConnectorService;
    @Autowired
    ProductManagementService productManagementService;
    @Autowired
    PropertiesPlaceholderConfiguration propertiesConfig;
    private static final Logger log = LoggerFactory.getLogger(ProductManagementController.class);

    @PostMapping("/product")
    public String hello(){
        return "hello";
    }

    @Observed
    @PostMapping(value = "/get-list-product" )
    public ResponseEntity<ResponseData> retrieveListOrderInfo(@RequestBody RequestData<DataUtil> requestData ) throws  Exception {
        log.info("Got a request");
        ResponseData<List<ProductListResponse>> responseData = new ResponseData<>();
        try {
            List<ProductListResponse> body = new ArrayList<>();
            System.out.println( ">>> requestData header >>>" + requestData.getHeader() );
            System.out.println( ">>> requestData body >>>" + requestData.getBody() );
            ResponseHeader header = new ResponseHeader("Y", "0000", "Success" );
            List<ProductDTO> orderList = productManagementService.retrieveListProductInfo();
            if ( orderList.size() > 0 ) {
                for ( ProductDTO product: orderList ) {
                    ProductListResponse orderListResponse = new ProductListResponse();
                    orderListResponse.setProductID(product.getProductID());
                    orderListResponse.setProductName(product.getProductName());
                    body.add(orderListResponse);
                }
            }
            RequestHeader requestHeader = new RequestHeader("somnang", "01", "en");
            RequestData requestBody = new RequestData<>(requestHeader, new DataUtil() );
            String url = propertiesConfig.getOrderService().concat( "/get-list-order");
            ResponseData<Map<String, Objects>> responseResult =  aPIConnectorService.postRequest( requestBody , url );

            if ( "N".equals( responseResult.getHeader().getSuccessYN() ) ) {
                header.setSuccessYN("N");
                header.setResultMessage(responseResult.getHeader().getResultMessage());
                header.setResultCode(responseResult.getHeader().getResultCode());
                body = new ArrayList<>();
            }

            System.out.println( responseResult.getBody().get("orderList"));

            responseData = new ResponseData<>(header, body);

        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return  ResponseEntity.ok( responseData );
    }
}
