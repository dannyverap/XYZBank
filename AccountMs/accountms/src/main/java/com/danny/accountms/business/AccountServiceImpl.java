package com.danny.accountms.business;

import com.danny.accountms.clients.CustomerClient;
import com.danny.accountms.clients.TransactionClient;
import com.danny.accountms.exception.BadPetitionException;
import com.danny.accountms.exception.NotFoundException;
import com.danny.accountms.model.Account;
import com.danny.accountms.model.AccountRequest;
import com.danny.accountms.model.AccountResponse;
import com.danny.accountms.model.ModelApiResponse;
import com.danny.accountms.model.Money;
import com.danny.accountms.model.MoneyTransfer;
import com.danny.accountms.model.TipoCuenta;
import com.danny.accountms.model.TransactionResponse;
import com.danny.accountms.repository.AccountRepository;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

  @Autowired
  AccountRepository accountRepository;
  @Autowired
  AccountMapper accountMapper;
  @Autowired
  CustomerClient customerClient;
  @Autowired
  TransactionClient transactionClient;

  @Override
  public AccountResponse createAccount(AccountRequest accountRequest) {
    Account account = this.accountMapper.getAccountFromRequest(accountRequest);
    this.customerClient.validateCustomerId(accountRequest.getClienteId());
    account.setSaldo(Math.max(accountRequest.getSaldo(), 0.0));
    account.setClienteId(accountRequest.getClienteId());
    account.setNumeroCuenta(
        this.generateUniqueNumeroCuenta(account.getClienteId(), account.getTipoCuenta()));

    this.accountRepository.save(account);
    return this.accountMapper.getAccountResponseFromAccount(account);
  }

  @Override
  public List<AccountResponse> getAccounts(int limit, int offset, UUID clienteId) {
    offset = Math.max(offset, 0);
    limit = (0 >= limit) ? 20 : limit;

    Pageable pageable = PageRequest.of(offset, limit);
    Page<Account> accounts = Optional.ofNullable(clienteId)
        .map(id -> accountRepository.findByClienteId(id, pageable))
        .orElseGet(() -> accountRepository.findAll(pageable));
    return accounts.stream().map(this.accountMapper::getAccountResponseFromAccount).toList();
  }

  @Override
  public AccountResponse getAccountDetails(UUID id) {
    return this.accountMapper.getAccountResponseFromAccount(this.accountRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Cuenta no encontrada")));
  }

  @Override
  public AccountResponse updateAccount(UUID id, AccountRequest newAccountData) {

    Account newAccountDataEntity = this.accountMapper.getAccountFromRequest(newAccountData);

    Account accountToUpdate = this.accountRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("no encontrado"));

    if (newAccountDataEntity.getNumeroCuenta() != null && !accountToUpdate.getNumeroCuenta()
        .equals(newAccountDataEntity.getNumeroCuenta())) {
      if (this.accountRepository.existsByNumeroCuenta(newAccountDataEntity.getNumeroCuenta())) {
        throw new BadPetitionException("El numero de cuenta debe ser única");
      }
      accountToUpdate.setNumeroCuenta(newAccountDataEntity.getNumeroCuenta());
    }

    if (newAccountDataEntity.getTipoCuenta() != null && !accountToUpdate.getTipoCuenta()
        .equals(newAccountDataEntity.getTipoCuenta())) {
      accountToUpdate.setTipoCuenta(newAccountDataEntity.getTipoCuenta());
    }

    if (newAccountDataEntity.getSaldo() != null && !accountToUpdate.getSaldo()
        .equals(newAccountDataEntity.getSaldo())) {
      accountToUpdate.setSaldo(newAccountDataEntity.getSaldo());
    }
    Account updatedAccount = this.accountRepository.save(accountToUpdate);
    return this.accountMapper.getAccountResponseFromAccount(updatedAccount);
  }

  @Override
  public ModelApiResponse deleteAccount(UUID id) {

    Account account = this.accountRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Cuenta no existe o ya fue borrada"));

    Double actualSaldo = account.getSaldo();
    if (actualSaldo > 0) {
      throw new BadPetitionException(
          "Retire el saldo antes de eliminar su cuenta. Saldo actual: " + actualSaldo);
    }

    if (actualSaldo < 0) {
      throw new BadPetitionException(
          "Pague la deuda antes de eliminar su cuenta. Saldo actual: " + actualSaldo);
    }

    this.accountRepository.deleteById(id);
    return this.createApiResponse("Cuenta borrada exitosamente");
  }

  @Override
  public ModelApiResponse depositeMoneyToAccount(UUID id, Money money) {
    Account account = this.accountRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));

    account.setSaldo(account.getSaldo() + Math.abs(money.getDinero()));
    this.accountRepository.save(account);
    TransactionResponse transactionResponse = this.transactionClient.registerDepositTransaction(
        money, account.getNumeroCuenta());

    return createApiResponse(
        "Operación exitosa, el nuevo saldo de la cuenta es: " + account.getSaldo() + ". "
            + "Transacción registrada con el ID: " + transactionResponse.getId());
  }

  @Override
  public ModelApiResponse withdrawMoneyFromAccount(UUID id, Money money) {
    Account account = this.accountRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));
    double newSaldo = getNewAccountSaldo(account, money.getDinero());
    account.setSaldo(newSaldo);
    this.accountRepository.save(account);
    TransactionResponse transactionResponse = this.transactionClient.registerWithdrawTransaction(
        money, account.getNumeroCuenta());
    return createApiResponse(
        "Operación exitosa, el nuevo saldo de la cuenta es: " + account.getSaldo() + ". "
            + "Transacción registrada con el ID: " + transactionResponse.getId());
  }

  @Override
  public ModelApiResponse transferMoneyBetweenAccounts(UUID id, MoneyTransfer moneyTransfer) {
    if (moneyTransfer.getDinero() == null || moneyTransfer.getDinero().isNaN()
        || moneyTransfer.getDinero() <= 0) {
      throw new BadPetitionException("Ingrese cantidad a transferir");
    }
    Account accountOrigin = this.accountRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Cuenta de destino no " + "encontrada"));
    Account accountDestination = this.accountRepository.findById(moneyTransfer.getIdCuentaDestino())
        .orElseThrow(() -> new NotFoundException("Cuenta de origen no encontrada"));

    double newAccountOriginSaldo = getNewAccountSaldo(accountOrigin, moneyTransfer.getDinero());
    accountOrigin.setSaldo(newAccountOriginSaldo);
    accountDestination.setSaldo(
        accountDestination.getSaldo() + Math.abs(moneyTransfer.getDinero()));
    this.accountRepository.save(accountDestination);
    this.accountRepository.save(accountDestination);
    TransactionResponse transactionResponse = this.transactionClient.registerTransferTransaction(
        this.accountMapper.convertMoneyTransferToMoney(moneyTransfer.getDinero()),
        accountDestination.getNumeroCuenta(), accountOrigin.getNumeroCuenta());
    return createApiResponse(
        "Operación exitosa, el nuevo saldo de la cuenta es: " + accountOrigin.getSaldo() + ". "
            + "Transacción registrada con el ID: " + transactionResponse.getId());
  }

  private double getNewAccountSaldo(Account accountOrigin, Double money) {
    double newAccountOriginSaldo = accountOrigin.getSaldo() - Math.abs(money);
    if (newAccountOriginSaldo < -500) {
      throw new BadPetitionException(
          "No puede retirar esa cantidad, su saldo actual es de :"
              + accountOrigin.getSaldo());
    }
    if (TipoCuenta.AHORRO.equals(accountOrigin.getTipoCuenta()) && newAccountOriginSaldo <= 0) {
      throw new BadPetitionException(
          "Su saldo no puede ser menor a 0 en una cuenta de Ahorro. Saldo actual:"
              + accountOrigin.getSaldo());
    }
    return newAccountOriginSaldo;
  }

  private ModelApiResponse createApiResponse(String message) {
    ModelApiResponse apiResponse = new ModelApiResponse();
    apiResponse.setMessage(message);
    return apiResponse;
  }

  private String generateUniqueNumeroCuenta(UUID clientId, TipoCuenta tipoCuenta) {
    String numeroCuenta;
    do {
      numeroCuenta = this.generateRandomNumeroCuenta(clientId, tipoCuenta);
    } while (this.accountRepository.existsByNumeroCuenta(numeroCuenta));
    return numeroCuenta;
  }

  private String generateRandomNumeroCuenta(UUID clientId, TipoCuenta tipoCuenta) {
    Random random = new Random();
    return clientId.toString().substring(0, 4) + tipoCuenta.toString().substring(0, 3)
        + random.nextInt(1000);
  }
}
