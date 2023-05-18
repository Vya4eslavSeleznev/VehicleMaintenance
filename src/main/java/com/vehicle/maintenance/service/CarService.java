package com.vehicle.maintenance.service;

import com.vehicle.maintenance.exception.CarNotFoundException;
import com.vehicle.maintenance.exception.CustomerNotFoundException;
import com.vehicle.maintenance.model.CarFindModel;
import com.vehicle.maintenance.model.CarModel;
import com.vehicle.maintenance.model.CarUpdateModel;

import java.util.List;

public interface CarService {

    void saveCar(CarModel carModel) throws CustomerNotFoundException;
    void deleteCar(long id);
    List<CarFindModel> findAllCars();
    CarFindModel findCarById(long id) throws CarNotFoundException;
    void updateCar(CarUpdateModel carUpdateModel) throws CarNotFoundException;
}
