package com.danny.CustomerMs.clients;

import com.danny.CustomerMs.exception.BadPetitionException;
import com.danny.CustomerMs.model.AccountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.UUID;

@Configuration
public class AccountClient {

    @Autowired
    private FeignAccountClient feignAccountClient;

    public List<AccountResponse> getAccountsByClientId(UUID id) {
        try {
            return this.feignAccountClient.getAccountsByClientId(id);
        } catch (HttpClientErrorException e) {
            throw new BadPetitionException("Error cuentas no encontradas");
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado: " + e.getMessage());
        }
    }

    public void DeleteAccount(UUID id) {
        try {
            this.feignAccountClient.DeleteAccount(id);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado: " + e.getMessage());
        }

    }
}
