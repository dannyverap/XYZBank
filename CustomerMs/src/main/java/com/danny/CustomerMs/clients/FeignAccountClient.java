package com.danny.CustomerMs.clients;

import com.danny.CustomerMs.model.AccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "ACCOUNTMS")
public interface FeignAccountClient {
    @GetMapping("/account")
    List<AccountResponse> getAccountsByClientId(@RequestParam("clienteId") UUID id);

    @DeleteMapping("/account/{id}")
    void DeleteAccount(@PathVariable("id") UUID id);
}
