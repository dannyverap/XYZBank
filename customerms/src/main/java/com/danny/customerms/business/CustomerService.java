package com.danny.customerms.business;

import com.danny.customerms.model.CustomerRequest;
import com.danny.customerms.model.CustomerResponse;
import com.danny.customerms.model.ModelApiResponse;
import java.util.List;
import java.util.UUID;

public interface CustomerService {

  CustomerResponse createCustomer(CustomerRequest customerRequest);

  List<CustomerResponse> getCustomers(int limit, int offset);

  CustomerResponse getCustomerDetails(UUID id);

  CustomerResponse updateCustomer(UUID id, CustomerRequest newCustomerData);

  ModelApiResponse deleteCustomer(UUID id);
}
