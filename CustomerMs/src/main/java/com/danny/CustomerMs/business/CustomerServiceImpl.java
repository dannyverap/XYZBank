package com.danny.CustomerMs.business;

import com.danny.CustomerMs.model.Customer;
import com.danny.CustomerMs.model.CustomerRequest;
import com.danny.CustomerMs.model.CustomerResponse;
import com.danny.CustomerMs.model.ModelApiResponse;
import com.danny.CustomerMs.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    private Boolean findIfCustomerExistsByEmail(String email) {
        return this.customerRepository.existsByEmail(email);
    }

    @Override
    public CustomerResponse createCustomer(CustomerRequest customerRequest) throws Exception {
        if (this.customerRepository.existsByEmail(customerRequest.getEmail())) {
            throw new Exception("Email ya registrado en DB");
        }
        if (this.customerRepository.existsByDni(customerRequest.getDni())) {
            throw new Exception("DNI ya registrado");
        }

        Customer customer = this.customerMapper.getCustomerFromRequest(customerRequest);
        this.customerRepository.save(customer);
        return this.customerMapper.getCustomerResponseFromCustomer(customer);
    }

    @Override
    public List<CustomerResponse> getCustomers(int limit, int offset) {
        Pageable pageable = PageRequest.of(offset, limit);
        List<Customer> customers = this.customerRepository.findAll();
        return customers.stream().map(this.customerMapper::getCustomerResponseFromCustomer).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public CustomerResponse getCustomerDetails(UUID id) {
        return this.customerMapper.getCustomerResponseFromCustomer(this.customerRepository.findById(id).get());
    }

    @Override
    public CustomerResponse updateCustomer(UUID id, CustomerRequest customerRequest) throws Exception {
        Optional<Customer> customerInDB = this.customerRepository.findById(id);

        if (customerInDB.isEmpty()) throw new Exception("Not found");
        if (this.findIfCustomerExistsByEmail(customerRequest.getEmail())) {
            throw new Exception("Email ya registrado en DB");
        }
        customerInDB.ifPresent(customer -> {
            if (!customer.getEmail().equals(customerRequest.getEmail())) {
                customer.setEmail(customerRequest.getEmail());
            }
            if (!customer.getNombre().equals(customerRequest.getNombre())) {
                customer.setNombre(customerRequest.getNombre());
            }
            if (!customer.getApellido().equals(customerRequest.getApellido())) {
                customer.setApellido(customerRequest.getApellido());
            }
        });
        Customer updatedCustomer = this.customerRepository.save(customerInDB.get());
        return this.customerMapper.getCustomerResponseFromCustomer(updatedCustomer);
    }

    @Override
    public ModelApiResponse deleteCustomer(UUID id) {
        ModelApiResponse response = new ModelApiResponse();
        if (!this.customerRepository.existsById(id)) {
            response.setMessage("Cliente no encontrado");
            return response;
        }
        this.customerRepository.deleteById(id);
        response.setMessage("Cliente borrado exitosamente");
        return response;
    }
}
