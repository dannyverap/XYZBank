package com.danny.CustomerMs.business;

import com.danny.CustomerMs.clients.RestAccountClient;
import com.danny.CustomerMs.exception.BadPetitionException;
import com.danny.CustomerMs.exception.ConflictException;
import com.danny.CustomerMs.exception.NotFoundException;
import com.danny.CustomerMs.model.*;
import com.danny.CustomerMs.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    RestAccountClient accountClient;

    @Override
    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        if (this.customerRepository.existsByEmail(customerRequest.getEmail())) {
            throw new ConflictException("Email ya registrado");
        }
        if (this.customerRepository.existsByDni(customerRequest.getDni())) {
            throw new ConflictException("DNI ya registrado");
        }
        Customer customer = this.customerMapper.getCustomerFromRequest(customerRequest);
        this.customerRepository.save(customer);
        return this.customerMapper.getCustomerResponseFromCustomer(customer);
    }

    //Todo validate integer limit and offset
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
        return this.customerMapper.getCustomerResponseFromCustomer(this.customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado")));
    }

    @Override
    public CustomerResponse updateCustomer(UUID id, CustomerRequest customerRequest) {
        Customer customerToUpdate = this.customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found"));

        if (!customerToUpdate.getEmail().equals(customerRequest.getEmail())) {
            if (this.customerRepository.existsByEmail(customerRequest.getEmail())) {
                throw new BadPetitionException("Email ya registrado en otro usuario");
            }
            customerToUpdate.setEmail(customerRequest.getEmail());
        }
        if (!customerToUpdate.getDni().equals(customerRequest.getDni())) {
            if (this.customerRepository.existsByDni(customerRequest.getDni())) {
                throw new BadPetitionException("DNI ya registrado en otro usuario");
            }
            customerToUpdate.setDni(customerRequest.getDni());
        }
        if (!customerToUpdate.getNombre().equals(customerRequest.getNombre())) {
            customerToUpdate.setNombre(customerRequest.getNombre());
        }
        if (!customerToUpdate.getApellido().equals(customerRequest.getApellido())) {
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

        if(this.findIfUserHaveActiveAccounts(accounts)){
            throw new BadPetitionException("Las cuentas bancarias deben tener un saldo igual a 0 para eliminar cliente");
        }

        this.sendOrderToDeleteAccounts(accounts);
        this.customerRepository.deleteById(id);
        ModelApiResponse response = new ModelApiResponse();
        response.setMessage("Cliente borrado exitosamente");
        return response;
    }

    private boolean findIfUserHaveActiveAccounts(List<AccountResponse> accounts) {
        return accounts.stream().anyMatch(account -> account.getSaldo() != 0.0 );
    }

    private void sendOrderToDeleteAccounts(List<AccountResponse> accounts) {
        accounts.forEach(account-> this.accountClient.DeleteAccount(account.getId()));
    }
}
