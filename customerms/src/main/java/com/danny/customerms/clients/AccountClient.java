package com.danny.customerms.clients;

import com.danny.customerms.exception.BadPetitionException;
import com.danny.customerms.model.AccountResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.HttpClientErrorException;

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
      this.feignAccountClient.deleteAccount(id);
    } catch (Exception e) {
      throw new RuntimeException("Error inesperado: " + e.getMessage());
    }

  }
}
