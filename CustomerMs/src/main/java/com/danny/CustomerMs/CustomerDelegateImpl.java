package com.danny.CustomerMs;

import com.danny.CustomerMs.api.CustomerApiDelegate;
import com.danny.CustomerMs.business.CustomerService;
import com.danny.CustomerMs.model.CustomerRequest;
import com.danny.CustomerMs.model.CustomerResponse;
import com.danny.CustomerMs.model.ModelApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CustomerDelegateImpl implements CustomerApiDelegate {

    @Autowired
    CustomerService customerService;


    @Override
    public ResponseEntity<CustomerResponse> createCustomer(CustomerRequest customerRequest) {
        return ResponseEntity.ok(this.customerService.createCustomer(customerRequest));
    }

    @Override
    public ResponseEntity<ModelApiResponse> deleteCustomer(UUID id) {
        return ResponseEntity.ok(this.customerService.deleteCustomer(id));
    }

    @Override
    public ResponseEntity<CustomerResponse> findCustomerById(UUID id) {
        return ResponseEntity.ok(this.customerService.getCustomerDetails(id));
    }

    @Override
    public ResponseEntity<List<CustomerResponse>> findCustomers(Integer offset, Integer limit) {
        return ResponseEntity.ok(this.customerService.getCustomers(limit, offset));
    }

    @Override
    public ResponseEntity<CustomerResponse> updateCustomer(UUID id, CustomerRequest customerRequest) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customerRequest));
    }
}
