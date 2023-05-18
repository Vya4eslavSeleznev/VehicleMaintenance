package com.vehicle.maintenance.service;

import com.vehicle.maintenance.exception.CarNotFoundException;
import com.vehicle.maintenance.exception.MaintenanceNotFoundException;
import com.vehicle.maintenance.model.MaintenanceFindModel;
import com.vehicle.maintenance.model.MaintenanceSaveModel;
import com.vehicle.maintenance.model.MaintenanceUpdateModel;

import java.util.List;

public interface MaintenanceService {

    void saveMaintenance(MaintenanceSaveModel maintenanceSaveModel) throws CarNotFoundException;
    void deleteMaintenance(long id);
    List<MaintenanceFindModel> findAllMaintenance();
    MaintenanceFindModel findMaintenanceById(long id) throws MaintenanceNotFoundException;
    void updateMaintenance(MaintenanceUpdateModel maintenanceUpdateModel) throws MaintenanceNotFoundException;
}
