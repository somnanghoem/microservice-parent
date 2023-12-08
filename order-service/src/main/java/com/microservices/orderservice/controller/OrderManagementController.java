package com.microservices.orderservice.controller;

import com.microservices.orderservice.config.PropertiesPlaceholderConfiguration;
import com.microservices.orderservice.dto.OrderDTO;
import com.microservices.orderservice.model.OrderListResponse;
import com.microservices.orderservice.service.APIConnectorService;
import com.microservices.orderservice.service.OrderManagementService;
import com.util.responseutil.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/order/v1")
public class OrderManagementController {

   @Autowired
   APIConnectorService aPIConnectorService;
   @Autowired
   OrderManagementService orderManagementService;
   @Autowired
   PropertiesPlaceholderConfiguration propertiesConfig;

    private static final Logger log = LoggerFactory.getLogger(OrderManagementController.class);

   @PostMapping("/get-list-order")
    public ResponseData<DataUtil>  retrieveListOrderInfo(@RequestBody RequestData<DataUtil> param ) throws  Exception {
        log.info(">>>>>>>>>> retrieveListOrderInfo>>>>>>>>>>");
       try {
           System.out.println( param );
           List<OrderListResponse> orderListResponseList = new ArrayList<>();
           ResponseHeader header = new ResponseHeader("Y", "0000", "Success" );
           List<OrderDTO> orderList = orderManagementService.retrieveListOrderInfo();
           if ( orderList.size() > 0 ) {
               for ( OrderDTO order: orderList ) {
                   OrderListResponse orderListResponse = new OrderListResponse();
                   orderListResponse.setOrderID(order.getOrderID());
                   orderListResponse.setOrderName(order.getOrderName());
                   orderListResponseList.add(orderListResponse);
               }
           }

           RequestHeader requestHeader = new RequestHeader("somnang", "01", "en");
           RequestData requestBody = new RequestData<>(requestHeader, new DataUtil() );
           String url = propertiesConfig.getOrderService().concat( "/get-list-product");
           ResponseData<Map<String, Objects>> responseResult =  aPIConnectorService.postRequest( requestBody , url );


           DataUtil body = new DataUtil();
           body.put("orderList",orderListResponseList);
           ResponseData<DataUtil> responseData = new ResponseData<>(header, body);
           return  responseData;
       } catch (Exception e ) {
           e.printStackTrace();
       }
       return null;
    }

}
