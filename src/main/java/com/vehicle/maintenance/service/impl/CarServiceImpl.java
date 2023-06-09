package com.vehicle.maintenance.service.impl;

import com.vehicle.maintenance.entity.Car;
import com.vehicle.maintenance.entity.Customer;
import com.vehicle.maintenance.entity.Maintenance;
import com.vehicle.maintenance.exception.CarNotFoundException;
import com.vehicle.maintenance.exception.CustomerNotFoundException;
import com.vehicle.maintenance.model.CarFindModel;
import com.vehicle.maintenance.model.CarSaveModel;
import com.vehicle.maintenance.model.CarUpdateModel;
import com.vehicle.maintenance.repository.CarRepository;
import com.vehicle.maintenance.repository.CustomerRepository;
import com.vehicle.maintenance.repository.MaintenanceRepository;
import com.vehicle.maintenance.service.CarService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService {

    private CarRepository carRepository;
    private MaintenanceRepository maintenanceRepository;
    private CustomerRepository customerRepository;

    @Override
    public void saveCar(CarSaveModel carModel) throws CustomerNotFoundException {
        Optional<Customer> customer = customerRepository.findById(carModel.getCustomerId());

        if(customer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        carRepository.save(
          new Car(carModel.getBrand(), carModel.getModel(), carModel.getEngine(), carModel.getColor(), customer.get())
        );
    }

    @Override
    @Transactional
    public void deleteCar(long id) throws CarNotFoundException {
        Optional<Car> car = carRepository.findById(id);

        if(car.isEmpty()) {
            throw new CarNotFoundException();
        }

        List<Maintenance> maintenanceList = maintenanceRepository.findByCarId(car.get().getId());

        maintenanceRepository.deleteByIdIn(
          maintenanceList
            .stream()
            .map(Maintenance::getId)
            .collect(Collectors.toList())
        );

        carRepository.deleteById(id);
    }

    @Override
    public List<CarFindModel> findAllCars() {
        return StreamSupport
          .stream(carRepository.findAll().spliterator(), false)
          .map(elem -> new CarFindModel(
            elem.getId(), elem.getBrand(), elem.getModel(), elem.getEngine(), elem.getColor(),
            elem.getCustomer().getName(), elem.getCustomer().getSurname(), elem.getCustomer().getLastName(),
            elem.getCustomer().getPhone(), elem.getCustomer().getBirthDate()
          ))
          .collect(Collectors.toList());
    }

    @Override
    public CarFindModel findCarById(long id) throws CarNotFoundException {
        Optional<Car> car = carRepository.findById(id);

        if(car.isEmpty()) {
            throw new CarNotFoundException();
        }

        return new CarFindModel(
          car.get().getId(), car.get().getBrand(), car.get().getModel(), car.get().getEngine(), car.get().getColor(),
          car.get().getCustomer().getName(), car.get().getCustomer().getSurname(),
          car.get().getCustomer().getLastName(), car.get().getCustomer().getPhone(),
          car.get().getCustomer().getBirthDate()
        );
    }

    @Override
    public void updateCar(CarUpdateModel carUpdateModel) throws CarNotFoundException {
        Optional<Car> car = carRepository.findById(carUpdateModel.getId());

        if(car.isEmpty()) {
            throw new CarNotFoundException();
        }

        car.get().setBrand(carUpdateModel.getBrand());
        car.get().setModel(carUpdateModel.getModel());
        car.get().setEngine(carUpdateModel.getEngine());
        car.get().setColor(carUpdateModel.getColor());

        carRepository.save(car.get());
    }
}
