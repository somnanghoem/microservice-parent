package com.microservices.productservice.service.impl;

import com.microservices.productservice.dto.ProductDTO;
import com.microservices.productservice.repository.ProductManagementDAO;
import com.microservices.productservice.service.ProductManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductManagementServiceImpl implements ProductManagementService {
    @Autowired
    ProductManagementDAO productManagementDAO;
    @Override
    public List<ProductDTO> retrieveListProductInfo() throws Exception {
        return productManagementDAO.retrieveListProductInfo();
    }
}
