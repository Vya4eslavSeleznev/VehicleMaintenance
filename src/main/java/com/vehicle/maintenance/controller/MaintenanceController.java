package com.vehicle.maintenance.controller;

import com.vehicle.maintenance.entity.Maintenance;
import com.vehicle.maintenance.exception.CarNotFoundException;
import com.vehicle.maintenance.exception.MaintenanceNotFoundException;
import com.vehicle.maintenance.model.MaintenanceFindModel;
import com.vehicle.maintenance.model.MaintenanceSaveModel;
import com.vehicle.maintenance.model.MaintenanceUpdateModel;
import com.vehicle.maintenance.service.MaintenanceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/maintenance")
@AllArgsConstructor
public class MaintenanceController {

    private MaintenanceService maintenanceService;

    @PostMapping
    public ResponseEntity<?> saveMaintenance(@RequestBody MaintenanceSaveModel maintenanceSaveModel) {
        try {
            maintenanceService.saveMaintenance(maintenanceSaveModel);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(CarNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMaintenance(@PathVariable long id) {
        maintenanceService.deleteMaintenance(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MaintenanceFindModel>> findAll() {
        return new ResponseEntity<>(maintenanceService.findAllMaintenance(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceFindModel> findMaintenanceById(@PathVariable long id) {
        try {
            return new ResponseEntity<>(maintenanceService.findMaintenanceById(id), HttpStatus.OK);
        }
        catch(MaintenanceNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/refreshment")
    public ResponseEntity<?> updateMaintenance(@RequestBody MaintenanceUpdateModel maintenanceUpdateModel) {
        try {
            maintenanceService.updateMaintenance(maintenanceUpdateModel);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(MaintenanceNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
