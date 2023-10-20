package com.microservices.orderservice.controller;

import com.microservices.orderservice.dto.OrderDTO;
import com.microservices.orderservice.model.OrderListResponse;
import com.microservices.orderservice.service.OrderManagementService;
import com.util.responseUtil.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class OrderManagementController {

   @Autowired
   OrderManagementService orderManagementService;

   @PostMapping("/get-list-order")
    public ResponseData<DataUtil>  retrieveListOrderInfo(@RequestBody RequestData<DataUtil> param ) throws  Exception {

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
       DataUtil body = new DataUtil();
       body.put("orderList",orderListResponseList);
       ResponseData<DataUtil> responseData = new ResponseData<>(header, body);
       return  responseData;
    }

}
