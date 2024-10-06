package com.danny.AccountMs.clients;

import com.danny.AccountMs.exception.BadPetitionException;
import com.danny.AccountMs.model.CustomerResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Configuration
public class RestCustomerClient {
    String Url = "http://localhost:8080/customer/";

    @Bean
    public RestTemplate restCustomerTemplate() {
        return new RestTemplate();
    }

    public void validateCustomerId(UUID id) {
        try {
            restCustomerTemplate().getForObject(this.Url + id.toString(), CustomerResponse.class);
        } catch (HttpClientErrorException e) {
            throw new BadPetitionException("El cliente no fue encontrado en el servicio de cuentas");
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado: " + e.getMessage());
        }
    }
}
