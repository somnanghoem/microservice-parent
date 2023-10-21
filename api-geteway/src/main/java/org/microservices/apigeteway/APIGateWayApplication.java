package org.microservices.apigeteway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class APIGateWayApplication {
      public static void main(String[] args) {
          SpringApplication.run(APIGateWayApplication.class, args);
      }

}
