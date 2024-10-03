package com.danny.CustomerMs.clients;

import com.danny.CustomerMs.exception.BadPetitionException;
import com.danny.CustomerMs.model.AccountResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Configuration
public class RestAccountClient {
    String Url = "http://localhost:8082/account";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public List<AccountResponse> getAccountsByClientId(UUID id) {
        try {
            return restTemplate().exchange(this.Url + "?clienteId=" + id, HttpMethod.GET, null, new ParameterizedTypeReference<List<AccountResponse>>() {
            }).getBody();
        } catch (HttpClientErrorException e) {
            throw new BadPetitionException("Error cuentas no encontradas");
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado: " + e.getMessage());
        }
    }

    public void DeleteAccount(UUID id) {
        try {
            restTemplate().delete(this.Url + "/" + id);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado: " + e.getMessage());
        }

    }
}
