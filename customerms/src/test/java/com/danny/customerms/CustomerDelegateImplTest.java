package com.danny.customerms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.danny.customerms.business.CustomerService;
import com.danny.customerms.model.CustomerRequest;
import com.danny.customerms.model.CustomerResponse;
import com.danny.customerms.model.ModelApiResponse;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class CustomerDelegateImplTest {

  @Mock
  private CustomerService customerService;

  @InjectMocks
  private CustomerDelegateImpl customerDelegate;

  private CustomerRequest customerRequest;
  private CustomerResponse customerResponse;

  @BeforeEach
  public void setUp() {
    customerRequest = new CustomerRequest();
    customerResponse = new CustomerResponse();
  }

  @Test
  public void testCreateCustomer() {
    given(customerService.createCustomer(any(CustomerRequest.class))).willReturn(customerResponse);

    ResponseEntity<CustomerResponse> response = customerDelegate.createCustomer(customerRequest);

    assertNotNull(response);
    assertEquals(200, response.getStatusCode().value());
    assertEquals(customerResponse, response.getBody());
  }

  @Test
  public void DeleteCustomer() {
    given(customerService.deleteCustomer(any(UUID.class))).willReturn(new ModelApiResponse());
    ResponseEntity<ModelApiResponse> response = customerDelegate.deleteCustomer(UUID.randomUUID());

    assertNotNull(response);
    assertEquals(200, response.getStatusCode().value());

  }

  @Test
  public void testGetCustomerDetails() {
    given(customerService.getCustomerDetails(any(UUID.class))).willReturn(new CustomerResponse());

    ResponseEntity<CustomerResponse> response = customerDelegate.findCustomerById(
        UUID.randomUUID());

    assertNotNull(response);
    assertEquals(200, response.getStatusCode().value());
    assertEquals(customerResponse, response.getBody());
  }

  @Test
  public void testUpdateCustomer() {
    given(customerService.updateCustomer(any(UUID.class), any(CustomerRequest.class))).willReturn(
        customerResponse);

    ResponseEntity<CustomerResponse> response = customerDelegate.updateCustomer(UUID.randomUUID(),
        customerRequest);

    assertNotNull(response);
    assertEquals(200, response.getStatusCode().value());
    assertEquals(customerResponse, response.getBody());
  }

  @Test
  public void testFindCustomers() {
    given(customerService.getCustomers(any(Integer.class), any(Integer.class))).willReturn(List.of(
        customerResponse));

    ResponseEntity<List<CustomerResponse>> response = customerDelegate.findCustomers(20, 0);

    assertNotNull(response);
    assertEquals(200, response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
  }
}
