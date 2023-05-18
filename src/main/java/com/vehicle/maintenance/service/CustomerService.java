package com.vehicle.maintenance.service;

import com.vehicle.maintenance.entity.Customer;
import com.vehicle.maintenance.exception.CustomerNotFoundException;
import com.vehicle.maintenance.exception.InvalidInputDataException;
import com.vehicle.maintenance.model.CustomerFindModel;
import com.vehicle.maintenance.model.CustomerSaveModel;
import com.vehicle.maintenance.model.CustomerUpdateModel;

import java.util.List;

public interface CustomerService {

    void saveCustomer(CustomerSaveModel customerSaveModel ) throws InvalidInputDataException;
    void deleteCustomer(long id) throws CustomerNotFoundException;
    List<CustomerFindModel> findAllCustomers();
    CustomerFindModel findCustomerById(long id) throws CustomerNotFoundException;
    void updateCustomer(CustomerUpdateModel customerUpdateModel) throws CustomerNotFoundException;
}
