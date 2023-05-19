package com.vehicle.maintenance.service;

import com.vehicle.maintenance.entity.Car;
import com.vehicle.maintenance.entity.Credential;
import com.vehicle.maintenance.entity.Customer;
import com.vehicle.maintenance.entity.Maintenance;
import com.vehicle.maintenance.exception.CarNotFoundException;
import com.vehicle.maintenance.exception.MaintenanceNotFoundException;
import com.vehicle.maintenance.model.MaintenanceFindModel;
import com.vehicle.maintenance.model.MaintenanceSaveModel;
import com.vehicle.maintenance.model.MaintenanceUpdateModel;
import com.vehicle.maintenance.model.Role;
import com.vehicle.maintenance.repository.CarRepository;
import com.vehicle.maintenance.repository.MaintenanceRepository;
import com.vehicle.maintenance.service.impl.MaintenanceServiceImpl;
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
public class MaintenanceServiceTest {

    @InjectMocks
    private MaintenanceServiceImpl maintenanceService;

    @Mock
    private MaintenanceRepository maintenanceRepository;

    @Mock
    private CarRepository carRepository;

    private Maintenance maintenance;
    private MaintenanceSaveModel maintenanceSaveModel;
    private MaintenanceUpdateModel maintenanceUpdateModel;
    private Car car;
    private long id;

    @BeforeEach
    public void init() {
        id = 1;
        Date date = new Date();
        String description = "description";

        maintenanceSaveModel = new MaintenanceSaveModel(id, date, description);
        maintenanceUpdateModel = new MaintenanceUpdateModel(id, date, description);

        Customer customer = new Customer("name", "surname", "lastname", "7999887766", date,
          new Credential(Role.USER, "pwd", "login"));

        car = new Car("brand", "model", "engine", "color", customer);
        maintenance = new Maintenance(maintenanceSaveModel.getDate(), maintenanceSaveModel.getDescription(), car);
    }

    @Test
    public void should_save_maintenance() throws CarNotFoundException {
        Optional<Car> carOpt = Optional.of(car);

        when(carRepository.findById(id)).thenReturn(carOpt);

        maintenanceService.saveMaintenance(maintenanceSaveModel);

        verify(maintenanceRepository, times(1)).save(any(Maintenance.class));
    }

    @Test
    public void should_save_maintenance_exception_returned() {
        assertThrows(CarNotFoundException.class, () -> maintenanceService.saveMaintenance(maintenanceSaveModel));
    }

    @Test
    public void should_delete_maintenance() {
        maintenanceService.deleteMaintenance(id);
        verify(maintenanceRepository, times(1)).deleteById(id);
    }

    @Test
    public void should_find_all_maintenance_list_returned() {
        Iterable<Maintenance> maintenanceList = List.of(maintenance);

        when(maintenanceRepository.findAll()).thenReturn(maintenanceList);

        List<MaintenanceFindModel> expectedList = StreamSupport
          .stream(maintenanceList.spliterator(), false)
          .map(elem -> new MaintenanceFindModel(
            elem.getId(), elem.getDate(), elem.getDescription(), elem.getCar().getBrand(), elem.getCar().getModel(),
            elem.getCar().getEngine(), elem.getCar().getColor(), elem.getCar().getCustomer().getName(),
            elem.getCar().getCustomer().getLastName(), elem.getCar().getCustomer().getPhone()
          ))
          .collect(Collectors.toList());

        List<MaintenanceFindModel> actualList = maintenanceService.findAllMaintenance();

        verify(maintenanceRepository, times(1)).findAll();

        assertEquals(expectedList.get(0).getId(), actualList.get(0).getId());
        assertEquals(expectedList.get(0).getDate(), actualList.get(0).getDate());
        assertEquals(expectedList.get(0).getDescription(), actualList.get(0).getDescription());
        assertEquals(expectedList.get(0).getBrand(), actualList.get(0).getBrand());
        assertEquals(expectedList.get(0).getModel(), actualList.get(0).getModel());
        assertEquals(expectedList.get(0).getEngine(), actualList.get(0).getEngine());
        assertEquals(expectedList.get(0).getColor(), actualList.get(0).getColor());
        assertEquals(expectedList.get(0).getName(), actualList.get(0).getName());
        assertEquals(expectedList.get(0).getLastName(), actualList.get(0).getLastName());
        assertEquals(expectedList.get(0).getPhone(), actualList.get(0).getPhone());
    }

    @Test
    public void should_find_maintenance_by_id_maintenance_find_model_returned() throws MaintenanceNotFoundException {
        Optional<Maintenance> maintenanceOpt = Optional.of(maintenance);

        when(maintenanceRepository.findById(id)).thenReturn(maintenanceOpt);

        MaintenanceFindModel expectedModel = new MaintenanceFindModel(
          maintenanceOpt.get().getId(), maintenanceOpt.get().getDate(), maintenanceOpt.get().getDescription(),
          maintenanceOpt.get().getCar().getBrand(), maintenanceOpt.get().getCar().getModel(),
          maintenanceOpt.get().getCar().getEngine(), maintenanceOpt.get().getCar().getColor(),
          maintenanceOpt.get().getCar().getCustomer().getName(), maintenanceOpt.get().getCar().getCustomer().getLastName(),
          maintenanceOpt.get().getCar().getCustomer().getPhone()
        );

        MaintenanceFindModel actualModel = maintenanceService.findMaintenanceById(id);

        verify(maintenanceRepository, times(1)).findById(id);

        assertEquals(expectedModel.getId(), actualModel.getId());
        assertEquals(expectedModel.getDate(), actualModel.getDate());
        assertEquals(expectedModel.getDescription(), actualModel.getDescription());
        assertEquals(expectedModel.getBrand(), actualModel.getBrand());
        assertEquals(expectedModel.getModel(), actualModel.getModel());
        assertEquals(expectedModel.getEngine(), actualModel.getEngine());
        assertEquals(expectedModel.getColor(), actualModel.getColor());
        assertEquals(expectedModel.getLastName(), actualModel.getLastName());
        assertEquals(expectedModel.getName(), actualModel.getName());
        assertEquals(expectedModel.getPhone(), actualModel.getPhone());
    }

    @Test
    public void should_find_maintenance_by_id_exception_returned() {
        assertThrows(MaintenanceNotFoundException.class, () -> maintenanceService.findMaintenanceById(id));
    }

    @Test
    public void should_update_maintenance() throws MaintenanceNotFoundException {
        Optional<Maintenance> maintenanceOpt = Optional.of(maintenance);

        when(maintenanceRepository.findById(id)).thenReturn(maintenanceOpt);

        maintenanceService.updateMaintenance(maintenanceUpdateModel);

        verify(maintenanceRepository, times(1)).findById(id);
        verify(maintenanceRepository, times(1)).save(any(Maintenance.class));
    }

    @Test
    public void should_update_maintenance_exception_returned() {
        assertThrows(MaintenanceNotFoundException.class, () ->
          maintenanceService.updateMaintenance(maintenanceUpdateModel)
        );
    }
}
