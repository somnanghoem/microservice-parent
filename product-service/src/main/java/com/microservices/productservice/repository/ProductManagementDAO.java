package com.microservices.productservice.repository;

import com.microservices.productservice.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductManagementDAO {

    public List<ProductDTO> retrieveListProductInfo() throws Exception;
}
