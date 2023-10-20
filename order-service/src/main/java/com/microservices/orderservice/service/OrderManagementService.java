package com.microservices.orderservice.service;

import com.microservices.orderservice.dto.OrderDTO;
import com.microservices.orderservice.model.OrderListResponse;

import java.util.List;

public interface OrderManagementService {

    public List<OrderDTO> retrieveListOrderInfo() throws Exception;
}
