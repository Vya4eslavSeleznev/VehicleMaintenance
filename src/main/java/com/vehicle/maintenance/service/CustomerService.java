package com.vehicle.maintenance.service;

import com.vehicle.maintenance.exception.CustomerNotFoundException;
import com.vehicle.maintenance.exception.InvalidInputDataException;
import com.vehicle.maintenance.model.CustomerSaveModel;

public interface CustomerService {

    void saveCustomer(CustomerSaveModel customerSaveModel ) throws InvalidInputDataException;
    void deleteCustomer(long id) throws CustomerNotFoundException;
}
