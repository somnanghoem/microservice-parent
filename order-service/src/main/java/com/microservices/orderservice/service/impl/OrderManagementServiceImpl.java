package com.microservices.orderservice.service.impl;

import com.microservices.orderservice.dto.OrderDTO;
import com.microservices.orderservice.repository.OrderManagementDAO;
import com.microservices.orderservice.service.OrderManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderManagementServiceImpl implements OrderManagementService {
    @Autowired
    OrderManagementDAO orderManagementDAO;
    @Override
    public List<OrderDTO> retrieveListOrderInfo() throws Exception {

        return orderManagementDAO.retrieveListOrderInfo();
    }
}
