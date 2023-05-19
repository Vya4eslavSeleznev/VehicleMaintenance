package com.vehicle.maintenance.service.impl;

import com.vehicle.maintenance.entity.Car;
import com.vehicle.maintenance.entity.Customer;
import com.vehicle.maintenance.entity.Maintenance;
import com.vehicle.maintenance.exception.CarNotFoundException;
import com.vehicle.maintenance.exception.CustomerNotFoundException;
import com.vehicle.maintenance.exception.MaintenanceNotFoundException;
import com.vehicle.maintenance.model.CarFindModel;
import com.vehicle.maintenance.model.MaintenanceFindModel;
import com.vehicle.maintenance.model.MaintenanceSaveModel;
import com.vehicle.maintenance.model.MaintenanceUpdateModel;
import com.vehicle.maintenance.repository.CarRepository;
import com.vehicle.maintenance.repository.MaintenanceRepository;
import com.vehicle.maintenance.service.MaintenanceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class MaintenanceServiceImpl implements MaintenanceService {

    private MaintenanceRepository maintenanceRepository;
    private CarRepository carRepository;

    @Override
    public void saveMaintenance(MaintenanceSaveModel maintenanceSaveModel) throws CarNotFoundException {
        Optional<Car> car = carRepository.findById(maintenanceSaveModel.getCarId());

        if(car.isEmpty()) {
            throw new CarNotFoundException();
        }

        maintenanceRepository.save(
          new Maintenance(maintenanceSaveModel.getDate(), maintenanceSaveModel.getDescription(), car.get())
        );
    }

    @Override
    public void deleteMaintenance(long id) {
        maintenanceRepository.deleteById(id);
    }

    @Override
    public List<MaintenanceFindModel> findAllMaintenance() {
        return StreamSupport
          .stream(maintenanceRepository.findAll().spliterator(), false)
          .map(elem -> new MaintenanceFindModel(
            elem.getId(), elem.getDate(), elem.getDescription(), elem.getCar().getBrand(), elem.getCar().getModel(),
            elem.getCar().getEngine(), elem.getCar().getColor(), elem.getCar().getCustomer().getName(),
            elem.getCar().getCustomer().getLastName(), elem.getCar().getCustomer().getPhone()
          ))
          .collect(Collectors.toList());
    }

    @Override
    public MaintenanceFindModel findMaintenanceById(long id) throws MaintenanceNotFoundException {
        Optional<Maintenance> maintenance = maintenanceRepository.findById(id);

        if(maintenance.isEmpty()) {
            throw new MaintenanceNotFoundException();
        }


        return new MaintenanceFindModel(
          maintenance.get().getId(), maintenance.get().getDate(), maintenance.get().getDescription(),
          maintenance.get().getCar().getBrand(), maintenance.get().getCar().getModel(),
          maintenance.get().getCar().getEngine(), maintenance.get().getCar().getColor(),
          maintenance.get().getCar().getCustomer().getName(), maintenance.get().getCar().getCustomer().getLastName(),
          maintenance.get().getCar().getCustomer().getPhone()
        );
    }

    @Override
    public void updateMaintenance(MaintenanceUpdateModel maintenanceUpdateModel) throws MaintenanceNotFoundException {
        Optional<Maintenance> maintenance = maintenanceRepository.findById(maintenanceUpdateModel.getId());

        if(maintenance.isEmpty()) {
            throw new MaintenanceNotFoundException();
        }

        maintenance.get().setDate(maintenanceUpdateModel.getDate());
        maintenance.get().setDescription(maintenanceUpdateModel.getDescription());

        maintenanceRepository.save(maintenance.get());
    }
}
