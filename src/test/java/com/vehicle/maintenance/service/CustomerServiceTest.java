package com.vehicle.maintenance.service;

import com.vehicle.maintenance.entity.Car;
import com.vehicle.maintenance.entity.Credential;
import com.vehicle.maintenance.entity.Customer;
import com.vehicle.maintenance.entity.Maintenance;
import com.vehicle.maintenance.exception.CustomerNotFoundException;
import com.vehicle.maintenance.exception.InvalidInputDataException;
import com.vehicle.maintenance.model.CustomerFindModel;
import com.vehicle.maintenance.model.CustomerSaveModel;
import com.vehicle.maintenance.model.CustomerUpdateModel;
import com.vehicle.maintenance.model.Role;
import com.vehicle.maintenance.repository.CarRepository;
import com.vehicle.maintenance.repository.CredentialRepository;
import com.vehicle.maintenance.repository.CustomerRepository;
import com.vehicle.maintenance.repository.MaintenanceRepository;
import com.vehicle.maintenance.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CredentialRepository credentialRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private MaintenanceRepository maintenanceRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Customer customer;

    private CustomerSaveModel customerSaveModel;
    private CustomerUpdateModel customerUpdateModel;
    private long id;
    private Date date;

    @BeforeEach
    public void init() {
        String name = "name";
        String surname = "surname";
        String lastName = "lastName";
        String phone = "7999887766";
        String username = "login";
        String password = "pwd";
        date = new Date();
        id = 1;

        Credential credential = new Credential(Role.USER, password, username);
        credential.setId(id);

        customer = new Customer(name, surname, lastName, phone, date, credential);
        customer.setId(id);

        customerSaveModel = new CustomerSaveModel(
          name, surname, lastName, phone, date, username, password
        );

        customerUpdateModel = new CustomerUpdateModel(
          id, name, surname, lastName, phone, date
        );
    }

    @Test
    public void should_save_customer() throws InvalidInputDataException {
        when(passwordEncoder.encode(customerSaveModel.getPassword())).thenReturn(customerSaveModel.getPassword());
        customerService.saveCustomer(customerSaveModel);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void should_save_customer_phone_with_letters_exception_returned() {
        customerSaveModel.setPhone("sdfsdfdsf");
        assertThrows(InvalidInputDataException.class, () -> customerService.saveCustomer(customerSaveModel));
    }

    @Test
    public void should_save_customer_name_with_digits_exception_returned() {
        customerSaveModel.setName("123123");
        assertThrows(InvalidInputDataException.class, () -> customerService.saveCustomer(customerSaveModel));
    }

    @Test
    public void should_delete_customer() throws CustomerNotFoundException {
        Optional<Customer> customerOpt = Optional.of(customer);
        Car car = new Car("brand", "model", "engine", "color", customer);
        Maintenance maintenance = new Maintenance(date, "description", car);
        car.setId(id);
        maintenance.setId(id);
        List<Car> carList = List.of(car);
        List<Maintenance> maintenanceList = List.of(maintenance);
        List<Long> ids = List.of(id);

        when(customerRepository.findById(id)).thenReturn(customerOpt);
        when(carRepository.findByCustomerId(id)).thenReturn(carList);
        when(maintenanceRepository.findByCarIdIn(ids)).thenReturn(maintenanceList);

        customerService.deleteCustomer(id);

        verify(customerRepository, times(1)).findById(id);
        verify(carRepository, times(1)).findByCustomerId(id);
        verify(maintenanceRepository, times(1)).findByCarIdIn(ids);

        verify(maintenanceRepository, times(1)).deleteByIdIn(ids);
        verify(carRepository, times(1)).deleteByIdIn(ids);
        verify(customerRepository, times(1)).deleteById(id);
        verify(credentialRepository, times(1)).deleteById(id);
    }

    @Test
    public void should_delete_customer_exception_returned() {
        assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomer(id));
    }

    @Test
    public void should_find_all_customers_list_returned() {
        Iterable<Customer> customers = List.of(customer);

        when(customerRepository.findAll()).thenReturn(customers);

        List<CustomerFindModel> expectedList = StreamSupport
          .stream(customers.spliterator(), false)
          .map(elem -> new CustomerFindModel(
            elem.getId(), elem.getName(), elem.getSurname(), elem.getLastName(), elem.getPhone(),
            elem.getBirthDate(), elem.getCredential().getUsername()
          ))
          .collect(Collectors.toList());

        List<CustomerFindModel> actualList = customerService.findAllCustomers();

        verify(customerRepository, times(1)).findAll();

        assertEquals(expectedList.get(0).getId(), actualList.get(0).getId());
        assertEquals(expectedList.get(0).getName(), actualList.get(0).getName());
        assertEquals(expectedList.get(0).getSurname(), actualList.get(0).getSurname());
        assertEquals(expectedList.get(0).getLastName(), actualList.get(0).getLastName());
        assertEquals(expectedList.get(0).getPhone(), actualList.get(0).getPhone());
        assertEquals(expectedList.get(0).getBirthDate(), actualList.get(0).getBirthDate());
        assertEquals(expectedList.get(0).getUsername(), actualList.get(0).getUsername());
    }

    @Test
    public void should_find_customer_by_id_customer_returned() throws CustomerNotFoundException {
        Optional<Customer> customerOpt = Optional.of(customer);

        when(customerRepository.findById(id)).thenReturn(customerOpt);

        CustomerFindModel expectedModel = new CustomerFindModel(
          customerOpt.get().getId(), customerOpt.get().getName(), customerOpt.get().getSurname(),
          customerOpt.get().getLastName(), customerOpt.get().getPhone(), customerOpt.get().getBirthDate(),
          customerOpt.get().getCredential().getUsername()
        );

        CustomerFindModel actualModel = customerService.findCustomerById(id);

        verify(customerRepository, times(1)).findById(id);

        assertEquals(expectedModel.getId(), actualModel.getId());
        assertEquals(expectedModel.getName(), actualModel.getName());
        assertEquals(expectedModel.getSurname(), actualModel.getSurname());
        assertEquals(expectedModel.getLastName(), actualModel.getLastName());
        assertEquals(expectedModel.getPhone(), actualModel.getPhone());
        assertEquals(expectedModel.getBirthDate(), actualModel.getBirthDate());
        assertEquals(expectedModel.getUsername(), actualModel.getUsername());
    }

    @Test
    public void should_find_customer_by_id_exception_returned() {
        assertThrows(CustomerNotFoundException.class, () -> customerService.findCustomerById(id));
    }

    @Test
    public void should_update_customer() throws CustomerNotFoundException {
        Optional<Customer> customerOpt = Optional.of(customer);

        when(customerRepository.findById(id)).thenReturn(customerOpt);

        customerService.updateCustomer(customerUpdateModel);

        verify(customerRepository, times(1)).findById(id);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void should_update_customer_exception_returned() {
        assertThrows(CustomerNotFoundException.class, () -> customerService.updateCustomer(customerUpdateModel));
    }
}
