package com.example.SystemAlerte.Controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.SystemAlerte.Model.Machine;
import com.example.SystemAlerte.Service.MachineService;

import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/Controller")
@RequiredArgsConstructor

public class MachineController {
    private final MachineService machineService;

    @GetMapping("/Dashboard/Machines")
    public ResponseEntity<List<Machine>> getMachines() {
        return ResponseEntity.ok().body(machineService.getMachines());

    }

    @PostMapping("/Dashboard/Machine/save")
    public ResponseEntity<Machine> SaveMachine(@RequestBody Machine Machine) {
        URI uri = URI.create(
                ServletUriComponentsBuilder.fromCurrentContextPath().path("/Controller/Dashboard/Machine/save").toUriString());

        return ResponseEntity.created(uri).body(machineService.SaveMachine(Machine));

    }

    @PutMapping("/Dashboard/Machine/update")
    public ResponseEntity<Machine> UpdateMachine(@RequestBody Machine Machine) {
        return ResponseEntity.ok().body(machineService.UpdateMachine(Machine));
    }

    @DeleteMapping("/Dashboard/Machine/delete/{id}")
    public void DeleteMachine(@PathVariable Long id) {
        machineService.DeleteMachine(id);
    }
}
