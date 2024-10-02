package com.danny.CustomerMs;

import com.danny.CustomerMs.api.CustomerApiDelegate;
import com.danny.CustomerMs.business.CustomerService;
import com.danny.CustomerMs.model.CustomerRequest;
import com.danny.CustomerMs.model.CustomerResponse;
import com.danny.CustomerMs.model.ModelApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerDelegateImpl implements CustomerApiDelegate {

    @Autowired
    CustomerService customerService;


    @Override
    public ResponseEntity<CustomerResponse> createCustomer(CustomerRequest customerRequest) {
        try {
            return ResponseEntity.ok(this.customerService.createCustomer(customerRequest));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<ModelApiResponse> deleteCustomer(UUID id) {
        try {
            return ResponseEntity.ok(this.customerService.deleteCustomer(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<CustomerResponse> findCustomerById(UUID id) {
        try {
            return ResponseEntity.ok(this.customerService.getCustomerDetails(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<List<CustomerResponse>> findCustomers(Integer offset, Integer limit) {
        try {
            return ResponseEntity.ok(this.customerService.getCustomers(limit, offset));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<CustomerResponse> updateCustomer(UUID id, CustomerRequest customerRequest) {
        try {
            return ResponseEntity.ok(customerService.updateCustomer(id, customerRequest));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
