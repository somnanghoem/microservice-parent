package com.microservices.productservice.service.impl;

import com.microservices.productservice.dto.ProductDTO;
import com.microservices.productservice.repository.ProductManagementDAO;
import com.microservices.productservice.service.ProductManagementService;
import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductManagementServiceImpl implements ProductManagementService {

    private  static final Logger log = LoggerFactory.getLogger(ProductManagementServiceImpl.class);
    @Autowired
    ProductManagementDAO productManagementDAO;

    @Observed(name = "user.name")
    @Override
    public List<ProductDTO> retrieveListProductInfo() throws Exception {
        log.info(">>>>retrieveListProductInfo>>>>");
        return productManagementDAO.retrieveListProductInfo();
    }
}
