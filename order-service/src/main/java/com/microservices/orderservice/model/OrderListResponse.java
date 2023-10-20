package com.microservices.orderservice.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class OrderListResponse {

    private String orderID = "";
    private String orderName = "";

}
