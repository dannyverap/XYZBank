package com.danny.customerms.business;

import com.danny.customerms.model.Customer;
import com.danny.customerms.model.CustomerRequest;
import com.danny.customerms.model.CustomerResponse;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

  public Customer getCustomerFromRequest(CustomerRequest customerRequest) {
    Customer customer = new Customer();
    customer.setNombre(customerRequest.getNombre());
    customer.setApellido(customerRequest.getApellido());
    customer.setDni(customerRequest.getDni());
    customer.setEmail(customerRequest.getEmail());
    return customer;
  }

  public CustomerResponse getCustomerResponseFromCustomer(Customer customer) {
    CustomerResponse customerResponse = new CustomerResponse();
    customerResponse.setId(customer.getId());
    customerResponse.setNombre(customer.getNombre());
    customerResponse.setApellido(customer.getApellido());
    customerResponse.setDni(customer.getDni());
    customerResponse.setEmail(customer.getEmail());
    return customerResponse;
  }
}
