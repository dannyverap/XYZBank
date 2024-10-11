package com.danny.accountms.clients;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CUSTOMERMS")
public interface FeignCustomerClient {

  @GetMapping("/customer/{id}")
  void getCustomer(@PathVariable("id") UUID id);
}
