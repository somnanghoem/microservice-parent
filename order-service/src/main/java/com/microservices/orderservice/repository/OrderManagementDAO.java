package com.microservices.orderservice.repository;

import com.microservices.orderservice.dto.OrderDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.core.annotation.Order;

import java.util.List;

@Mapper
public interface OrderManagementDAO {

    public List<OrderDTO> retrieveListOrderInfo() throws  Exception;
}
