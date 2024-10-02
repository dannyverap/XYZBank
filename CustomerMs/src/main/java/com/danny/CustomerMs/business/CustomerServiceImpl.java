package com.danny.CustomerMs.business;

import com.danny.CustomerMs.exception.BadPetitionException;
import com.danny.CustomerMs.exception.ConflictException;
import com.danny.CustomerMs.exception.NotFoundException;
import com.danny.CustomerMs.model.Customer;
import com.danny.CustomerMs.model.CustomerRequest;
import com.danny.CustomerMs.model.CustomerResponse;
import com.danny.CustomerMs.model.ModelApiResponse;
import com.danny.CustomerMs.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CustomerMapper customerMapper;

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
        offset = (offset >= 0) ? offset: 0;
        limit= ( limit > 0 ) ? limit: 20;
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Customer> customers = this.customerRepository.findAll(pageable);
        return customers.stream().map(this.customerMapper::getCustomerResponseFromCustomer).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public CustomerResponse getCustomerDetails(UUID id) {
        return this.customerMapper.getCustomerResponseFromCustomer(this.customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado")));
    }

    @Override
    public CustomerResponse updateCustomer(UUID id, CustomerRequest customerRequest) {
        Optional<Customer> customerToUpdate = this.customerRepository.findById(id);

        if (customerToUpdate.isEmpty()) {
            throw new NotFoundException("Not found");
        }

        customerToUpdate.ifPresent(customer -> {
            if (!customer.getEmail().equals(customerRequest.getEmail())) {
                if (this.customerRepository.existsByEmail(customerRequest.getEmail())) {
                    throw new BadPetitionException("Email ya registrado en otro usuario");
                }
                customer.setEmail(customerRequest.getEmail());
            }
            if (!customer.getDni().equals(customerRequest.getDni())) {
                if (this.customerRepository.existsByDni(customerRequest.getDni())) {
                    throw new BadPetitionException("DNI ya registrado en otro usuario");
                }
                customer.setDni(customerRequest.getDni());
            }
            if (!customer.getNombre().equals(customerRequest.getNombre())) {
                customer.setNombre(customerRequest.getNombre());
            }
            if (!customer.getApellido().equals(customerRequest.getApellido())) {
                customer.setApellido(customerRequest.getApellido());
            }
        });
        Customer updatedCustomer = this.customerRepository.save(customerToUpdate.get());
        return this.customerMapper.getCustomerResponseFromCustomer(updatedCustomer);
    }

    @Override
    public ModelApiResponse deleteCustomer(UUID id) {
        if (!this.customerRepository.existsById(id)) {
            throw new NotFoundException("Cliente no existe y ya se encuentra eliminado");
        }
        this.customerRepository.deleteById(id);
        ModelApiResponse response = new ModelApiResponse();
        response.setMessage("Cliente borrado exitosamente");
        return response;
    }
}
