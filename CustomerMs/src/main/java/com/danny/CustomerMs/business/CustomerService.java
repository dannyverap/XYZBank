package com.danny.CustomerMs.business;

import com.danny.CustomerMs.model.CustomerRequest;
import com.danny.CustomerMs.model.CustomerResponse;
import com.danny.CustomerMs.model.ModelApiResponse;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    CustomerResponse createCustomer (CustomerRequest customer);
    List<CustomerResponse> getCustomers(int Limit, int offset);
    CustomerResponse getCustomerDetails(UUID id);
    CustomerResponse updateCustomer(UUID id, CustomerRequest newCustomerData);
    ModelApiResponse deleteCustomer(UUID id);
}
