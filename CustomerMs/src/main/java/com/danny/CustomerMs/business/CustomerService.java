package com.danny.CustomerMs.business;

import com.danny.CustomerMs.model.CustomerRequest;
import com.danny.CustomerMs.model.CustomerResponse;
import com.danny.CustomerMs.model.ModelApiResponse;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    CustomerResponse createCustomer (CustomerRequest customer) throws Exception;;
    List<CustomerResponse> getCustomers(int Limit, int offset) throws Exception;;
    CustomerResponse getCustomerDetails(UUID id) throws Exception;;
    CustomerResponse updateCustomer(UUID id, CustomerRequest newCustomerData) throws Exception;
    ModelApiResponse deleteCustomer(UUID id) throws Exception;
}
