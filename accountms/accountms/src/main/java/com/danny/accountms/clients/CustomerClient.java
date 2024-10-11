package com.danny.accountms.clients;

import com.danny.accountms.exception.BadPetitionException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.HttpClientErrorException;

@Configuration
public class CustomerClient {

  @Autowired
  private FeignCustomerClient customerFeignClient;

  public void validateCustomerId(UUID id) {
    try {
      this.customerFeignClient.getCustomer(id);
    } catch (HttpClientErrorException e) {
      throw new BadPetitionException("El cliente no fue encontrado en el servicio de cuentas");
    } catch (Exception e) {
      throw new RuntimeException("Error inesperado: " + e.getMessage());
    }
  }
}
