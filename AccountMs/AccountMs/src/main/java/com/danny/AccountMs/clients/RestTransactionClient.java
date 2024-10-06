package com.danny.AccountMs.clients;

import com.danny.AccountMs.exception.BadPetitionException;
import com.danny.AccountMs.model.Money;
import com.danny.AccountMs.model.TransactionRequest;
import com.danny.AccountMs.model.TransactionResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Configuration
public class RestTransactionClient {
    String Url = "http://localhost:8080/transaction/";

    @Bean
    public RestTemplate restTransactionTemplate() {
        return new RestTemplate();
    }

    public TransactionResponse registerDepositTransaction(Money money, String cuentaDestino) {
        TransactionRequest transactionRequest = this.createTransactionRequest(money,
                                                                              TransactionRequest.TipoEnum.DEPOSITO,
                                                                              cuentaDestino,
                                                                              "");
        try {
            return this.restTransactionTemplate()
                       .postForEntity(this.Url + "deposito", transactionRequest, TransactionResponse.class)
                       .getBody();
        } catch (Exception e) {
            throw new BadPetitionException("Error cuentas no encontradas");
        }
    }

    public TransactionResponse registerWithdrawTransaction(Money money, String cuentaDestino) {
        TransactionRequest transactionRequest = this.createTransactionRequest(money,
                                                                              TransactionRequest.TipoEnum.RETIRO,
                                                                              cuentaDestino,
                                                                              "");
        try {
            return this.restTransactionTemplate()
                       .postForEntity(this.Url + "retiro", transactionRequest, TransactionResponse.class)
                       .getBody();
        } catch (Exception e) {
            throw new BadPetitionException("Error cuentas no encontradas");
        }
    }

    public TransactionResponse registerTransferTransaction(Money money, String cuentaDestino, String cuentaOrigen) {
        TransactionRequest transactionRequest = this.createTransactionRequest(money,
                                                                              TransactionRequest.TipoEnum.TRANSFERENCIA,
                                                                              cuentaDestino,
                                                                              cuentaOrigen);
        try {
            return this.restTransactionTemplate()
                       .postForEntity(this.Url + "transferencia", transactionRequest, TransactionResponse.class)
                       .getBody();
        } catch (Exception e) {
            throw new BadPetitionException("Error cuentas no encontradas");
        }
    }

    private TransactionRequest createTransactionRequest(Money money,
                                                        TransactionRequest.TipoEnum tipoTransaction,
                                                        String cuentaDestino,
                                                        String cuentaOrigen) {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setMonto(money.getDinero());
        transactionRequest.setCuentaDestino(cuentaDestino);
        transactionRequest.setTipo(tipoTransaction);
        transactionRequest.setFecha(LocalDate.now());
        if (tipoTransaction.equals(TransactionRequest.TipoEnum.TRANSFERENCIA) && (cuentaOrigen == null || cuentaOrigen.isBlank())) {
            throw new BadPetitionException("Ingrese la cuenta de Origen");
        }
        return transactionRequest;
    }
}
