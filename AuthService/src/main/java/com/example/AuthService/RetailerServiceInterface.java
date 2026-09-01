package com.example.AuthService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "retailer-service",
        url = "http://localhost:8082"
)
@Service
public interface RetailerServiceInterface {

    @PostMapping("/retailers")
    void createretailer(@RequestBody Retailerdto retailerobj);
}