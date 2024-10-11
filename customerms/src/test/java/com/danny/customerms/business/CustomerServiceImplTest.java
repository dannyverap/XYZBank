package com.danny.customerms.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import com.danny.customerms.clients.AccountClient;
import com.danny.customerms.exception.BadPetitionException;
import com.danny.customerms.exception.ConflictException;
import com.danny.customerms.exception.NotFoundException;
import com.danny.customerms.model.AccountResponse;
import com.danny.customerms.model.Customer;
import com.danny.customerms.model.CustomerRequest;
import com.danny.customerms.model.CustomerResponse;
import com.danny.customerms.model.ModelApiResponse;
import com.danny.customerms.repository.CustomerRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

  @Mock
  private CustomerRepository customerRepository;

  @Spy
  private CustomerMapper customerMapper;

  @Mock
  private AccountClient accountClient;

  @InjectMocks
  private CustomerServiceImpl customerService;

  private Customer customer;
  private CustomerRequest customerRequest;
  private CustomerResponse customerResponse;

  @BeforeEach
  public void setUp() {
    customerRequest = createCustomerRequest();
    customer = createCustomer(customerRequest);
    customerResponse = createCustomerResponse(customer);
  }

  @Test
  @DisplayName("Test crear cliente")
  public void testCreateCustomer() {
    given(customerRepository.existsByEmail(customerRequest.getEmail())).willReturn(false);
    given(customerRepository.existsByDni(customerRequest.getDni())).willReturn(false);

    given(customerRepository.save(any(Customer.class))).willReturn(customer);

    CustomerResponse response = customerService.createCustomer(customerRequest);
    assertNotNull(response);
  }

  @Test
  @DisplayName("Test crear cliente - Arroja error cuando el email ya está registrado")
  public void testCreateCustomer_ThrowsErrorWhenEmailExists() {
    given(customerRepository.existsByEmail(customerRequest.getEmail())).willReturn(true);

    ConflictException exception = assertThrows(ConflictException.class,
        () -> customerService.createCustomer(customerRequest));

    assertEquals("Email ya registrado", exception.getMessage());
  }

  @Test
  @DisplayName("Test crear cliente - Arroja error cuando el DNI ya está registrado")
  public void testCreateCustomer_ThrowsErrorWhenDniExists() {
    given(customerRepository.existsByDni(customerRequest.getDni())).willReturn(true);

    ConflictException exception = assertThrows(ConflictException.class,
        () -> customerService.createCustomer(customerRequest));

    assertEquals("DNI ya registrado", exception.getMessage());
  }

  @Test
  @DisplayName("Test obtener detalles del cliente")
  public void testGetCustomerDetails() {
    given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));

    CustomerResponse response = customerService.getCustomerDetails(customer.getId());

    assertNotNull(response);
    assertEquals(customerResponse, response);
  }

  @Test
  @DisplayName("Test obtener detalles del cliente - Cliente no encontrado")
  public void testGetCustomerDetails_ThrowsErrorWhenCustomerNotFound() {
    given(customerRepository.findById(customer.getId())).willReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> customerService.getCustomerDetails(customer.getId()));

    assertEquals("Cliente no encontrado", exception.getMessage());
  }

  @Test
  @DisplayName("Test listar clientes")
  public void testGetCustomers() {
    List<Customer> customerList = List.of(customer);
    Page<Customer> customerPage = new PageImpl<>(customerList);

    given(customerRepository.findAll(PageRequest.of(0, 20))).willReturn(customerPage);

    List<CustomerResponse> responses = customerService.getCustomers(20, 0);

    assertNotNull(responses);
    assertEquals(1, responses.size());
    assertEquals(customerResponse, responses.get(0));
  }

  @Test
  @DisplayName("Test listar clientes - límite negativo")
  public void testGetCustomersWithNegativeLimit() {
    List<Customer> customerList = List.of(customer);
    Page<Customer> accountPage = new PageImpl<>(customerList);

    given(customerRepository.findAll(PageRequest.of(0, 20))).willReturn(accountPage);
    given(customerMapper.getCustomerResponseFromCustomer(customer)).willReturn(customerResponse);

    List<CustomerResponse> responses = customerService.getCustomers(-5, 0);

    assertNotNull(responses);
    assertEquals(1, responses.size());
    assertEquals(customerResponse, responses.get(0));
  }

  @Test
  @DisplayName("Test actualizar cliente")
  public void testUpdateCustomer() {
    given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));
    given(customerRepository.save(customer)).willReturn(customer);

    CustomerResponse response = customerService.updateCustomer(customer.getId(), customerRequest);

    assertNotNull(response);
    assertEquals(customerResponse, response);
  }

  @Test
  @DisplayName("Test actualizar cliente - cliente no encontrado")
  public void testUpdateCustomerNotFound() {
    UUID id = UUID.randomUUID();
    given(customerRepository.findById(id)).willReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class, () -> {
      customerService.updateCustomer(id, customerRequest);
    });

    assertEquals("Not found", exception.getMessage());
  }

  @Test
  @DisplayName("Test actualizar cliente - email ya registrado")
  public void testUpdateCustomerEmailConflict() {
    customerRequest.setEmail("newemail@example.com");
    given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));
    given(customerRepository.existsByEmail(customerRequest.getEmail())).willReturn(true);

    BadPetitionException exception = assertThrows(BadPetitionException.class, () -> {
      customerService.updateCustomer(customer.getId(), customerRequest);
    });

    assertEquals("Email ya registrado en otro usuario", exception.getMessage());
  }

  @Test
  @DisplayName("Test actualizar cliente - DNI ya registrado")
  public void testUpdateCustomerDniConflict() {
    customerRequest.setDni("87654321");
    given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));
    given(customerRepository.existsByDni(customerRequest.getDni())).willReturn(true);

    BadPetitionException exception = assertThrows(BadPetitionException.class, () -> {
      customerService.updateCustomer(customer.getId(), customerRequest);
    });

    assertEquals("DNI ya registrado en otro usuario", exception.getMessage());
  }

  @Test
  @DisplayName("Test actualizar cliente - éxito con cambios")
  public void testUpdateCustomerChangeNombre() {
    customerRequest.setNombre("Danny");
    customerRequest.setApellido("NTT");
    customerRequest.setEmail("ntt@example.com");
    customerResponse.setDni("77020212");

    given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));
    given(customerRepository.save(customer)).willReturn(customer);
    given(customerMapper.getCustomerResponseFromCustomer(customer)).willReturn(customerResponse);

    CustomerResponse response = customerService.updateCustomer(customer.getId(), customerRequest);

    assertNotNull(response);
    assertEquals(customerResponse, response);
  }

  @Test
  @DisplayName("Test eliminar cliente con cuentas activas - debería lanzar excepción")
  public void testDeleteCustomerWithActiveAccounts() {
    given(customerRepository.existsById(customer.getId())).willReturn(true);
    given(accountClient.getAccountsByClientId(customer.getId())).willReturn(
        List.of(createAccountResponse(100.0)));

    BadPetitionException exception = assertThrows(BadPetitionException.class,
        () -> customerService.deleteCustomer(customer.getId()));

    assertEquals("Las cuentas bancarias deben tener un saldo igual a 0 para eliminar cliente",
        exception.getMessage());
  }

  @Test
  @DisplayName("Test eliminar cliente con cuentas con saldo 0 - No debería lanzar excepción")
  public void testDeleteCustomerWithZeroBalanceAccounts() {
    given(customerRepository.existsById(customer.getId())).willReturn(true);
    given(accountClient.getAccountsByClientId(customer.getId())).willReturn(
        List.of(createAccountResponse(0.0)));
    doNothing().when(accountClient).DeleteAccount(any(UUID.class));

    ModelApiResponse response = customerService.deleteCustomer(customer.getId());

    assertNotNull(response);
    assertEquals("Cliente borrado exitosamente", response.getMessage());
  }

  private CustomerRequest createCustomerRequest() {
    CustomerRequest request = new CustomerRequest();
    request.setEmail("test@example.com");
    request.setDni("12345678");
    request.setNombre("John");
    request.setApellido("Doe");
    return request;
  }

  private Customer createCustomer(CustomerRequest request) {
    Customer customer = new Customer();
    customer.setId(UUID.randomUUID());
    customer.setEmail(request.getEmail());
    customer.setDni(request.getDni());
    customer.setNombre(request.getNombre());
    customer.setApellido(request.getApellido());
    return customer;
  }

  private CustomerResponse createCustomerResponse(Customer customer) {
    CustomerResponse response = new CustomerResponse();
    response.setId(customer.getId());
    response.setEmail(customer.getEmail());
    response.setDni(customer.getDni());
    response.setNombre(customer.getNombre());
    response.setApellido(customer.getApellido());
    return response;
  }

  private AccountResponse createAccountResponse(double saldo) {
    AccountResponse response = new AccountResponse();
    response.setId(UUID.randomUUID());
    response.setSaldo(saldo);
    return response;
  }
}
