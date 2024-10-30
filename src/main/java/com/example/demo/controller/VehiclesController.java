package com.example.demo.controller;

import com.example.demo.entity.Vehicles;

import com.example.demo.repository.VehiclesRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/vehicles")
public class VehiclesController {
	
	@Autowired
    private VehiclesRepository vehiclesRepository;

  
    @PostMapping
    public Vehicles createVehicles(@RequestBody Vehicles vehicles) {
        return vehiclesRepository.save(vehicles);
    }
    
    @GetMapping
    public List<Vehicles> getAllVehicles() {
        return vehiclesRepository.findAll();
    }


    
    @GetMapping("/{id}")
    public ResponseEntity<Vehicles> getVehiclesById(@PathVariable Long id) {
        return vehiclesRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

  
    @PutMapping("/{id}")
    public ResponseEntity<Vehicles> updateVehicles(@PathVariable Long id, @RequestBody Vehicles vehiclesDetails) {
        return vehiclesRepository.findById(id)
                .map(vehicles -> {
                    vehicles.setMake(vehiclesDetails.getMake());
                    vehicles.setModel(vehiclesDetails.getModel());
                    vehicles.setYear(vehiclesDetails.getYear());
                    return ResponseEntity.ok(vehiclesRepository.save(vehicles));
                    
                })
                .orElse(ResponseEntity.notFound().build());
    }

 
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteVehicles(@PathVariable Long id) {
        return vehiclesRepository.findById(id)
                .map(vehicles -> {
                    vehiclesRepository.delete(vehicles);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    
    @GetMapping("/paginated")
    public Page<Vehicles> getVehiclesPaginated(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return vehiclesRepository.findAll(pageable);
    }

   
    @GetMapping("/sorted")
    public Iterable<Vehicles> getVehiclesSorted(@RequestParam String sortBy, @RequestParam String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        return vehiclesRepository.findAll(sort);
    }

    
    @GetMapping("/paginated-sorted")
    public Page<Vehicles> getVehiclesPaginatedAndSorted(@RequestParam int page, @RequestParam int size,
                                                         @RequestParam String sortBy, @RequestParam String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return vehiclesRepository.findAll(pageable);
    }

}
