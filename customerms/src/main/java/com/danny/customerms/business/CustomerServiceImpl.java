package com.danny.customerms.business;

import com.danny.customerms.clients.AccountClient;
import com.danny.customerms.exception.BadPetitionException;
import com.danny.customerms.exception.ConflictException;
import com.danny.customerms.exception.NotFoundException;
import com.danny.customerms.model.AccountResponse;
import com.danny.customerms.model.Customer;
import com.danny.customerms.model.CustomerRequest;
import com.danny.customerms.model.CustomerResponse;
import com.danny.customerms.model.ModelApiResponse;
import com.danny.customerms.repository.CustomerRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

  @Autowired
  CustomerRepository customerRepository;
  @Autowired
  CustomerMapper customerMapper;
  @Autowired
  AccountClient accountClient;

  @Override
  public CustomerResponse createCustomer(CustomerRequest customerRequest) {
    Customer customer = this.customerMapper.getCustomerFromRequest(customerRequest);

    if (this.customerRepository.existsByEmail(customerRequest.getEmail())) {
      throw new ConflictException("Email ya registrado");
    }
    if (this.customerRepository.existsByDni(customerRequest.getDni())) {
      throw new ConflictException("DNI ya registrado");
    }

    this.customerRepository.save(customer);
    return this.customerMapper.getCustomerResponseFromCustomer(customer);
  }

  @Override
  public List<CustomerResponse> getCustomers(int limit, int offset) {
    offset = Math.max(offset, 0);
    limit = (0 >= limit) ? 20 : limit;
    Pageable pageable = PageRequest.of(offset, limit);
    Page<Customer> customers = this.customerRepository.findAll(pageable);
    return customers.stream().map(this.customerMapper::getCustomerResponseFromCustomer).toList();
  }

  @Override
  public CustomerResponse getCustomerDetails(UUID id) {
    return this.customerMapper.getCustomerResponseFromCustomer(this.customerRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Cliente no encontrado")));
  }

  @Override
  public CustomerResponse updateCustomer(UUID id, CustomerRequest customerRequest) {
    Customer customerToUpdate = this.customerRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Not found"));

    if (customerRequest.getEmail() != null && !customerToUpdate.getEmail()
        .equals(customerRequest.getEmail())) {
      if (this.customerRepository.existsByEmail(customerRequest.getEmail())) {
        throw new BadPetitionException("Email ya registrado en otro usuario");
      }
      customerToUpdate.setEmail(customerRequest.getEmail());
    }
    if (customerRequest.getDni() != null && !customerToUpdate.getDni()
        .equals(customerRequest.getDni())) {
      if (this.customerRepository.existsByDni(customerRequest.getDni())) {
        throw new BadPetitionException("DNI ya registrado en otro usuario");
      }
      customerToUpdate.setDni(customerRequest.getDni());
    }
    if (customerRequest.getNombre() != null && !customerToUpdate.getNombre()
        .equals(customerRequest.getNombre())) {
      customerToUpdate.setNombre(customerRequest.getNombre());
    }
    if (customerRequest.getApellido() != null && !customerToUpdate.getApellido()
        .equals(customerRequest.getApellido())) {
      customerToUpdate.setApellido(customerRequest.getApellido());
    }

    Customer updatedCustomer = this.customerRepository.save(customerToUpdate);
    return this.customerMapper.getCustomerResponseFromCustomer(updatedCustomer);
  }

  @Override
  public ModelApiResponse deleteCustomer(UUID id) {

    if (!this.customerRepository.existsById(id)) {
      throw new NotFoundException("Cliente no existe o ya se encuentra eliminado");
    }

    List<AccountResponse> accounts = this.accountClient.getAccountsByClientId(id);

    if (this.findIfUserHaveActiveAccounts(accounts)) {
      throw new BadPetitionException(
          "Las cuentas bancarias deben tener un saldo igual a 0 para eliminar " + "cliente");
    }
    this.sendOrderToDeleteAccounts(accounts);
    this.customerRepository.deleteById(id);
    ModelApiResponse response = new ModelApiResponse();
    response.setMessage("Cliente borrado exitosamente");
    return response;
  }

  private boolean findIfUserHaveActiveAccounts(List<AccountResponse> accounts) {
    return accounts.stream().anyMatch(account -> account.getSaldo() != 0.0);
  }

  private void sendOrderToDeleteAccounts(List<AccountResponse> accounts) {
    accounts.forEach(account -> this.accountClient.DeleteAccount(account.getId()));
  }
}
