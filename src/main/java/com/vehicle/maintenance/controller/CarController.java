package com.vehicle.maintenance.controller;

import com.vehicle.maintenance.exception.CarNotFoundException;
import com.vehicle.maintenance.exception.CustomerNotFoundException;
import com.vehicle.maintenance.model.CarFindModel;
import com.vehicle.maintenance.model.CarSaveModel;
import com.vehicle.maintenance.model.CarUpdateModel;
import com.vehicle.maintenance.service.CarService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/car")
@AllArgsConstructor
public class CarController {

    private CarService carService;

    @PostMapping
    public ResponseEntity<?> saveCar(@RequestBody CarSaveModel carSaveModel) {
        try {
            carService.saveCar(carSaveModel);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(CustomerNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable long id) {
        carService.deleteCar(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CarFindModel>> findAllCars() {
        return new ResponseEntity<>(carService.findAllCars(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarFindModel> findCarById(@PathVariable long id) {
        try {
            return new ResponseEntity<>(carService.findCarById(id), HttpStatus.OK);
        }
        catch(CarNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/refreshment")
    public ResponseEntity<?> updateCar(@RequestBody CarUpdateModel carUpdateModel) {
        try {
            carService.updateCar(carUpdateModel);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(CarNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
