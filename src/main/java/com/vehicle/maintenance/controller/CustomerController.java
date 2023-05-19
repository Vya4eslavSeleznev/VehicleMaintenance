package com.vehicle.maintenance.controller;

import com.vehicle.maintenance.exception.CustomerNotFoundException;
import com.vehicle.maintenance.exception.InvalidInputDataException;
import com.vehicle.maintenance.model.CustomerFindModel;
import com.vehicle.maintenance.model.CustomerSaveModel;
import com.vehicle.maintenance.model.CustomerUpdateModel;
import com.vehicle.maintenance.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@AllArgsConstructor
public class CustomerController {

    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<?> saveCustomer(@RequestBody CustomerSaveModel customerSaveModel) {
        try {
            customerService.saveCustomer(customerSaveModel);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(InvalidInputDataException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable long id) {
        try {
            customerService.deleteCustomer(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(CustomerNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<CustomerFindModel>> findAll() {
        return new ResponseEntity<>(customerService.findAllCustomers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerFindModel> findCustomerById(@PathVariable long id) {
        try {
            return new ResponseEntity<>(customerService.findCustomerById(id), HttpStatus.OK);
        }
        catch(CustomerNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/refreshment")
    public ResponseEntity<?> updateCustomer(@RequestBody CustomerUpdateModel customerUpdateModel) {
        try {
            customerService.updateCustomer(customerUpdateModel);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(CustomerNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
