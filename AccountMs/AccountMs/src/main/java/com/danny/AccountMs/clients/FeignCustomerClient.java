package com.danny.AccountMs.clients;

import com.danny.AccountMs.model.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "CUSTOMERMS")
public interface FeignCustomerClient {
    @GetMapping("/customer/{id}")
    CustomerResponse getCustomer(@PathVariable("id") UUID id);
}
