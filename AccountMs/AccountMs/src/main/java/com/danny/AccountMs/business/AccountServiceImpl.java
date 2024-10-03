package com.danny.AccountMs.business;

import com.danny.AccountMs.exception.BadPetitionException;
import com.danny.AccountMs.exception.NotFoundException;
import com.danny.AccountMs.model.*;
import com.danny.AccountMs.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountMapper accountMapper;

    @Override
    public AccountResponse createAccount(AccountRequest accountRequest) {
        //Validar si usuario existe
        //luego crear cuenta
        Account account = this.accountMapper.getAccountFromRequest(accountRequest);
        account.setSaldo(0.0);
        account.setClientId(accountRequest.getClienteId());
        account.setNumeroCuenta(this.generateUniqueNumeroCuenta(account.getClientId(), account.getTipoCuenta()));
        this.accountRepository.save(account);
        return this.accountMapper.getAccountResponseFromAccount(account);
    }

    @Override
    public List<AccountResponse> getAccounts(int limit, int offset) {
        offset = Math.max(offset, 0);
        limit = (0 >= limit) ? 20 : limit;
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Account> accounts = this.accountRepository.findAll(pageable);
        return accounts.stream().map(this.accountMapper::getAccountResponseFromAccount).toList();
    }

    @Override
    public AccountResponse getAccountDetails(UUID id) {
        return this.accountMapper.getAccountResponseFromAccount(this.accountRepository.findById(id).orElseThrow(() -> new NotFoundException("Cuenta no encontrada")));
    }

    @Override
    public AccountResponse updateAccount(UUID id, AccountRequest newAccountData) {
        return null;
    }

    @Override
    public ModelApiResponse deleteAccount(UUID id) {
        Optional<Account> account = this.accountRepository.findById(id);
        if (account.isEmpty()) {
            throw new NotFoundException("Cuenta no existe o ya fue borrada");
        }
        Double actualSaldo = account.get().getSaldo();
        if (actualSaldo >0 )
            throw new BadPetitionException("Retire el saldo antes de eliminar su cuenta. Saldo actual: " + actualSaldo);
        if (actualSaldo > 0 )
            throw new BadPetitionException("Pague la deuda antes de eliminar su cuenta. Saldo actual: " + actualSaldo);
        this.accountRepository.deleteById(id);
        ModelApiResponse response = new ModelApiResponse();
        response.setMessage("Cuenta borrada exitosamente");
        return response;
    }

    @Override
    public ModelApiResponse depositeMoneyToAccount(UUID id, Money money) {
        Account account = this.accountRepository.findById(id).orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));
        account.setSaldo(account.getSaldo() + money.getDinero());
        this.accountRepository.save(account);
        ModelApiResponse apiResponse = new ModelApiResponse();
        apiResponse.setMessage("Operación exitosa, el nuevo saldo de la cuenta es:" + account.getSaldo());
        return apiResponse;
    }

    @Override
    public ModelApiResponse withdrawMoneyFromAccount(UUID id, Money money) {
        Account account = this.accountRepository.findById(id).orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));
        double newSaldo = account.getSaldo() - money.getDinero();
        if (newSaldo < -500)
            throw new BadPetitionException("No puede retirar esa cantidad, su saldo actual es de :" + account.getSaldo());
        if (TipoCuenta.AHORRO.equals(account.getTipoCuenta()) && newSaldo < 0)
            throw new BadPetitionException("Su saldo no puede ser menor a 0 en una cuenta de Ahorro. Saldo actual:" + account.getSaldo());
        account.setSaldo(newSaldo);
        this.accountRepository.save(account);
        ModelApiResponse apiResponse = new ModelApiResponse();
        apiResponse.setMessage("Operación exitosa, el nuevo saldo de la cuenta es: " + account.getSaldo());
        return apiResponse;
    }

    private String generateUniqueNumeroCuenta(UUID clientId, TipoCuenta tipoCuenta) {
        String numeroCuenta;
        do {
            numeroCuenta = this.generateRandomeNumeroCuenta(clientId, tipoCuenta);
        } while (this.accountRepository.existsByNumeroCuenta(numeroCuenta));
        return numeroCuenta;
    }

    private String generateRandomeNumeroCuenta(UUID clientId, TipoCuenta tipoCuenta) {
        Random random = new Random();
        return clientId.toString().substring(0, 4) + tipoCuenta.toString().substring(0, 3) + random.nextInt(1000);
    }
}
