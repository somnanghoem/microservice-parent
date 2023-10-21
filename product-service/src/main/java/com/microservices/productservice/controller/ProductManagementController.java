package com.microservices.productservice.controller;


import com.microservices.productservice.dto.ProductDTO;
import com.microservices.productservice.model.ProductListResponse;
import com.microservices.productservice.service.APIConnectorService;
import com.microservices.productservice.service.ProductManagementService;
import com.util.responseutil.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
public class ProductManagementController {

    @Autowired
    APIConnectorService aPIConnectorService;
    @Autowired
    ProductManagementService productManagementService;
    @PostMapping("/get-list-product")
    public ResponseEntity<ResponseData> retrieveListOrderInfo() throws  Exception {

        List<ProductListResponse> body = new ArrayList<>();
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

        ResponseData<Map<String, Objects>> responseResult =  aPIConnectorService.postRequest( requestBody , "http://127.0.0.1:6061/api/v1/get-list-order" );

       System.out.println( responseResult.getBody().get("orderList"));

        ResponseData<List<ProductListResponse>> responseData = new ResponseData<>(header, body);
        return  ResponseEntity.ok( responseData );
    }
}
