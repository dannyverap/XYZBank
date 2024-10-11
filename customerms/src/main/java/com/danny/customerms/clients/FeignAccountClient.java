package com.danny.customerms.clients;

import com.danny.customerms.model.AccountResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ACCOUNTMS")
public interface FeignAccountClient {

  @GetMapping("/account")
  List<AccountResponse> getAccountsByClientId(@RequestParam("clienteId") UUID id);

  @DeleteMapping("/account/{id}")
  void deleteAccount(@PathVariable("id") UUID id);
}
