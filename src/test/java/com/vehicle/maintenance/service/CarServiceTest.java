package com.vehicle.maintenance.service;

import com.vehicle.maintenance.entity.Car;
import com.vehicle.maintenance.entity.Credential;
import com.vehicle.maintenance.entity.Customer;
import com.vehicle.maintenance.entity.Maintenance;
import com.vehicle.maintenance.exception.CarNotFoundException;
import com.vehicle.maintenance.exception.CustomerNotFoundException;
import com.vehicle.maintenance.model.*;
import com.vehicle.maintenance.repository.CarRepository;
import com.vehicle.maintenance.repository.CustomerRepository;
import com.vehicle.maintenance.repository.MaintenanceRepository;
import com.vehicle.maintenance.service.impl.CarServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
public class CarServiceTest {

    @InjectMocks
    private CarServiceImpl carService;

    @Mock
    private CarRepository carRepository;

    @Mock
    private MaintenanceRepository maintenanceRepository;

    @Mock
    private CustomerRepository customerRepository;

    private CarSaveModel carSaveModel;
    private Car car;
    private Customer customer;
    private long id;
    private Date date;
    private String brand;
    private String model;
    private String engine;
    private String color;
    private CarUpdateModel carUpdateModel;

    @BeforeEach
    public void init() {
        id = 1;
        brand = "brand";
        model = "model";
        engine = "engine";
        color = "color";
        date = new Date();
        carSaveModel = new CarSaveModel(brand, model, engine, color, id);
        carUpdateModel = new CarUpdateModel(id, brand, model, engine, color);

        customer = new Customer(
          "name", "surname", "lastname", "phone", date,
          new Credential(Role.USER, "pwd", "login")
        );

        car = new Car(brand, model, engine, color, customer);
    }

    @Test
    public void should_save_car() throws CustomerNotFoundException {
        Optional<Customer> customerOpt = Optional.of(customer);

        when(customerRepository.findById(id)).thenReturn(customerOpt);

        carService.saveCar(carSaveModel);

        verify(carRepository, times(1)).save(any(Car.class));
        verify(customerRepository, times(1)).findById(id);
    }

    @Test
    public void should_save_car_exception_returned() {
        assertThrows(CustomerNotFoundException.class, () -> carService.saveCar(carSaveModel));
    }

    @Test
    public void should_delete_car() throws CarNotFoundException {
        car.setId(id);
        Optional<Car> carOpt = Optional.of(car);
        Maintenance maintenance = new Maintenance(date, "description", car);
        maintenance.setId(id);

        List<Maintenance> maintenanceList = List.of(maintenance);

        when(carRepository.findById(id)).thenReturn(carOpt);
        when(maintenanceRepository.findByCarId(id)).thenReturn(maintenanceList);

        carService.deleteCar(id);

        verify(carRepository, times(1)).findById(id);
        verify(maintenanceRepository, times(1)).findByCarId(id);
        verify(maintenanceRepository, times(1)).deleteByIdIn(List.of(id));
        verify(carRepository, times(1)).deleteById(id);
    }

    @Test
    public void should_delete_car_exception_returned() {
        assertThrows(CarNotFoundException.class, () -> carService.deleteCar(id));
    }

    @Test
    public void should_find_all_cars() {
        Iterable<Car> cars = List.of(car);

        when(carRepository.findAll()).thenReturn(cars);

        List<CarFindModel> expectedList = StreamSupport
          .stream(cars.spliterator(), false)
          .map(elem -> new CarFindModel(
            elem.getId(), elem.getBrand(), elem.getModel(), elem.getEngine(), elem.getColor(),
            elem.getCustomer().getName(), elem.getCustomer().getSurname(), elem.getCustomer().getLastName(),
            elem.getCustomer().getPhone(), elem.getCustomer().getBirthDate()
          ))
          .collect(Collectors.toList());

        List<CarFindModel> actualList = carService.findAllCars();

        verify(carRepository, times(1)).findAll();

        assertEquals(expectedList.get(0).getId(), actualList.get(0).getId());
        assertEquals(expectedList.get(0).getBrand(), actualList.get(0).getBrand());
        assertEquals(expectedList.get(0).getModel(), actualList.get(0).getModel());
        assertEquals(expectedList.get(0).getEngine(), actualList.get(0).getEngine());
        assertEquals(expectedList.get(0).getColor(), actualList.get(0).getColor());
        assertEquals(expectedList.get(0).getName(), actualList.get(0).getName());
        assertEquals(expectedList.get(0).getLastName(), actualList.get(0).getLastName());
        assertEquals(expectedList.get(0).getPhone(), actualList.get(0).getPhone());
        assertEquals(expectedList.get(0).getBirthDate(), actualList.get(0).getBirthDate());
    }

    @Test
    public void should_find_car_by_id() throws CarNotFoundException {
        Optional<Car> carOpt = Optional.of(car);

        when(carRepository.findById(id)).thenReturn(carOpt);

        CarFindModel expectedModel = new CarFindModel(
          carOpt.get().getId(), carOpt.get().getBrand(), carOpt.get().getModel(), carOpt.get().getEngine(),
          carOpt.get().getColor(), carOpt.get().getCustomer().getName(), carOpt.get().getCustomer().getSurname(),
          carOpt.get().getCustomer().getLastName(), carOpt.get().getCustomer().getPhone(),
          carOpt.get().getCustomer().getBirthDate()
        );

        CarFindModel actualModel = carService.findCarById(id);

        verify(carRepository, times(1)).findById(id);

        assertEquals(expectedModel.getId(), actualModel.getId());
        assertEquals(expectedModel.getBrand(), actualModel.getBrand());
        assertEquals(expectedModel.getModel(), actualModel.getModel());
        assertEquals(expectedModel.getEngine(), actualModel.getEngine());
        assertEquals(expectedModel.getColor(), actualModel.getColor());
        assertEquals(expectedModel.getName(), actualModel.getName());
        assertEquals(expectedModel.getLastName(), actualModel.getLastName());
        assertEquals(expectedModel.getPhone(), actualModel.getPhone());
        assertEquals(expectedModel.getBirthDate(), actualModel.getBirthDate());
    }

    @Test
    public void should_find_car_by_id_exception_returned() {
        assertThrows(CarNotFoundException.class, () -> carService.deleteCar(id));
    }

    @Test
    public void should_update_car() throws CarNotFoundException {
        Optional<Car> carOpt = Optional.of(car);

        when(carRepository.findById(id)).thenReturn(carOpt);

        carService.updateCar(carUpdateModel);

        verify(carRepository, times(1)).findById(id);
        verify(carRepository, times(1)).save(any(Car.class));

    }

    @Test
    public void should_update_car_exception_returned() {
        assertThrows(CarNotFoundException.class, () -> carService.updateCar(carUpdateModel));
    }
}
















