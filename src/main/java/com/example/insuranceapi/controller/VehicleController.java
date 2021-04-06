package com.example.insuranceapi.controller;

import com.example.insuranceapi.converter.DTOConverter;
import com.example.insuranceapi.exception.ResourceNotFoundException;
import com.example.insuranceapi.model.Vehicle;
import com.example.insuranceapi.service.VehicleService;
import com.example.insuranceapi.dto.VehicleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/api/v1/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private DTOConverter dtoConverter;

    @GetMapping("/list")
    public Page<VehicleDTO> list(@RequestParam(name = "page", defaultValue = "0") int page,
                                 @RequestParam(name = "size", defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "id");
        Page<Vehicle> pageResult = vehicleService.getVehiclePages(pageRequest);
        List<VehicleDTO> vehicleDTOs = pageResult
                .stream()
                .map(l -> dtoConverter.convertToVehicleDTO(l))
                .collect(toList());

        return new PageImpl<>(vehicleDTOs, pageRequest, pageResult.getTotalElements());
    }

    @GetMapping("/allvehicles")
    public List<VehicleDTO> getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getVehicles();
        return vehicles
                .stream()
                .map(l -> dtoConverter.convertToVehicleDTO(l))
                .collect(toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getVehicleByID(@PathVariable(value = "id") Integer vehicleId) throws ResourceNotFoundException {
        Vehicle vehicle = vehicleService.getVehicle(vehicleId);
        return ResponseEntity.ok().body(dtoConverter.convertToVehicleDTO(vehicle));
    }

    @PostMapping
    public Vehicle createVehicle(@RequestBody VehicleDTO vehicleDTO) {
        return vehicleService.saveVehicle(dtoConverter.convertToVehicle(vehicleDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleDTO> updateVehicle(@Valid @RequestBody VehicleDTO vehicleDTO, @PathVariable(value = "id") Integer vehicleId) throws ResourceNotFoundException {
        Vehicle vehicle = dtoConverter.convertToVehicle(vehicleDTO);
        vehicle.setId(vehicleId);
        Vehicle updatedVehicle = vehicleService.updateVehicle(vehicleId, vehicle);
        return ResponseEntity.ok(dtoConverter.convertToVehicleDTO(updatedVehicle));
    }

    @DeleteMapping("/{vehicleId}")
    public Map<String, Boolean> deleteVehicle(@PathVariable("vehicleId") Integer vehicleId) throws Exception {
        vehicleService.deleteVehicle(vehicleId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
