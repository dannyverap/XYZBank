package com.danny.customerms;

import com.danny.customerms.api.CustomerApiDelegate;
import com.danny.customerms.business.CustomerService;
import com.danny.customerms.model.CustomerRequest;
import com.danny.customerms.model.CustomerResponse;
import com.danny.customerms.model.ModelApiResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

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
  public ResponseEntity<List<CustomerResponse>> findCustomers(Integer limit, Integer offset) {
    return ResponseEntity.ok(this.customerService.getCustomers(limit, offset));
  }

  @Override
  public ResponseEntity<CustomerResponse> updateCustomer(UUID id, CustomerRequest customerRequest) {
    return ResponseEntity.ok(customerService.updateCustomer(id, customerRequest));
  }
}
