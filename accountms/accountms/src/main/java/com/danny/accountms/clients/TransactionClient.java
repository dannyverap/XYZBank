package com.danny.accountms.clients;

import com.danny.accountms.exception.BadPetitionException;
import com.danny.accountms.model.Money;
import com.danny.accountms.model.TransactionRequest;
import com.danny.accountms.model.TransactionResponse;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TransactionClient {

  @Autowired
  private FeignTransactionClient feignTransactionClient;

  @Bean
  public RestTemplate restTransactionTemplate() {
    return new RestTemplate();
  }

  public TransactionResponse registerDepositTransaction(Money money, String cuentaDestino) {
    TransactionRequest transactionRequest = this.createTransactionRequest(money,
        TransactionRequest.TipoEnum.DEPOSITO,
        cuentaDestino,
        "");
    return this.feignTransactionClient.registerDepositTransaction(transactionRequest);
  }

  public TransactionResponse registerWithdrawTransaction(Money money, String cuentaDestino) {
    TransactionRequest transactionRequest = this.createTransactionRequest(money,
        TransactionRequest.TipoEnum.RETIRO,
        cuentaDestino,
        "");
    return this.feignTransactionClient.registerWithdrawTransaction(transactionRequest);
  }

  public TransactionResponse registerTransferTransaction(Money money, String cuentaDestino,
      String cuentaOrigen) {
    TransactionRequest transactionRequest = this.createTransactionRequest(money,
        TransactionRequest.TipoEnum.TRANSFERENCIA,
        cuentaDestino,
        cuentaOrigen);
    return this.feignTransactionClient.registerTransferTransaction(transactionRequest);
  }

  private TransactionRequest createTransactionRequest(Money money,
      TransactionRequest.TipoEnum tipoTransaction,
      String cuentaDestino,
      String cuentaOrigen) {

    if (tipoTransaction.equals(TransactionRequest.TipoEnum.TRANSFERENCIA) && (cuentaOrigen == null
        || cuentaOrigen.isBlank())) {
      throw new BadPetitionException("Ingrese la cuenta de Origen");
    }
    TransactionRequest transactionRequest = new TransactionRequest();
    transactionRequest.setMonto(money.getDinero());
    transactionRequest.setCuentaDestino(cuentaDestino);
    transactionRequest.setCuentaOrigen(cuentaOrigen);
    transactionRequest.setTipo(tipoTransaction);
    transactionRequest.setFecha(LocalDate.now());

    return transactionRequest;
  }
}
