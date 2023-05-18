package com.vehicle.maintenance.service.impl;

import com.vehicle.maintenance.entity.Car;
import com.vehicle.maintenance.entity.Credential;
import com.vehicle.maintenance.entity.Customer;
import com.vehicle.maintenance.entity.Maintenance;
import com.vehicle.maintenance.exception.CustomerNotFoundException;
import com.vehicle.maintenance.exception.InvalidInputDataException;
import com.vehicle.maintenance.model.CustomerInfoModel;
import com.vehicle.maintenance.model.CustomerSaveModel;
import com.vehicle.maintenance.model.CustomerUpdateModel;
import com.vehicle.maintenance.model.Role;
import com.vehicle.maintenance.repository.CarRepository;
import com.vehicle.maintenance.repository.CredentialRepository;
import com.vehicle.maintenance.repository.CustomerRepository;
import com.vehicle.maintenance.repository.MaintenanceRepository;
import com.vehicle.maintenance.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;
    private CredentialRepository credentialRepository;
    private CarRepository carRepository;
    private MaintenanceRepository maintenanceRepository;

    @Override
    public void saveCustomer(CustomerSaveModel customerSaveModel) throws InvalidInputDataException {
        if(customerSaveModel.getName().matches("[0-9]+") ||
          customerSaveModel.getSurname().matches("[0-9]+") ||
          customerSaveModel.getLastName().matches("[0-9]+") ||
          customerSaveModel.getPhone().matches("[a-zA-Z]+")) {
            throw new InvalidInputDataException();
        }

        customerRepository.save(
          new Customer(
            customerSaveModel.getName(), customerSaveModel.getSurname(), customerSaveModel.getLastName(),
            customerSaveModel.getPhone(), customerSaveModel.getBirthDate(),
            new Credential(Role.USER, customerSaveModel.getPassword(), customerSaveModel.getUsername())
          )
        );
    }

    @Override
    @Transactional
    public void deleteCustomer(long id) throws CustomerNotFoundException {
        Optional<Customer> customer = customerRepository.findById(id);

        if(customer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        long customerId = customer.get().getId();
        long credentialId = customer.get().getCredential().getId();

        List<Car> cars = carRepository.findByCustomerId(customerId);

        List<Long> carsId = cars
          .stream()
          .map(Car::getId)
          .collect(Collectors.toList());


        List<Maintenance> listOfMaintenance = maintenanceRepository.findByCarIdIn(carsId);

        List<Long> maintenanceIds = listOfMaintenance
          .stream()
          .map(Maintenance::getId)
          .collect(Collectors.toList());

        maintenanceRepository.deleteByIdIn(maintenanceIds);
        carRepository.deleteByIdIn(carsId);
        customerRepository.deleteById(customerId);
        credentialRepository.deleteById(credentialId);
    }

    @Override
    public List<CustomerInfoModel> findAllCustomers() {
        return StreamSupport
          .stream(customerRepository.findAll().spliterator(), false)
          .map(elem -> new CustomerInfoModel(
            elem.getName(), elem.getSurname(), elem.getLastName(), elem.getPhone(),
            elem.getBirthDate(), elem.getCredential().getUsername()
          ))
          .collect(Collectors.toList());
    }

    @Override
    public CustomerInfoModel findCustomerById(long id) throws CustomerNotFoundException {
        Optional<Customer> customer = customerRepository.findById(id);

        if(customer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        return new CustomerInfoModel(
          customer.get().getName(), customer.get().getSurname(), customer.get().getLastName(),
          customer.get().getPhone(), customer.get().getBirthDate(), customer.get().getCredential().getUsername()
        );
    }

    @Override
    public void updateCustomer(CustomerUpdateModel customerUpdateModel) throws CustomerNotFoundException {
        Optional<Customer> customer = customerRepository.findById(customerUpdateModel.getId());

        if(customer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        customer.get().setName(customerUpdateModel.getName());
        customer.get().setSurname(customerUpdateModel.getSurname());
        customer.get().setLastName(customerUpdateModel.getLastName());
        customer.get().setPhone(customerUpdateModel.getPhone());
        customer.get().setBirthDate(customerUpdateModel.getBirthDate());

        customerRepository.save(customer.get());
    }
}
