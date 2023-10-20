package com.microservices.productservice.service;

import com.microservices.productservice.dto.ProductDTO;

import java.util.List;

public interface ProductManagementService {
    public List<ProductDTO> retrieveListProductInfo() throws Exception;
}
