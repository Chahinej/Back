package com.example.SystemAlerte.Controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.SystemAlerte.Model.Incident;
import com.example.SystemAlerte.Service.IncidentService;

import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/Controller")
@RequiredArgsConstructor

public class IncidentController {
    private final IncidentService incidentservice;

    @GetMapping("/Front/Incidents")
    public ResponseEntity<List<Incident>> getIncidents() {
        return ResponseEntity.ok().body(incidentservice.getIncidents());

    }

    @PostMapping("/Dashboard/Incident/save")
    public ResponseEntity<Incident> SaveIncident(@RequestBody Incident Incident) {
        URI uri = URI.create(
                ServletUriComponentsBuilder.fromCurrentContextPath().path("/Controller/Dashboard/Incident/save").toUriString());

        return ResponseEntity.created(uri).body(incidentservice.SaveIncident(Incident));

    }

    @PutMapping("/Dashboard/Incident/update")
    public ResponseEntity<Incident> UpdateIncident(@RequestBody Incident Incident) {
        return ResponseEntity.ok().body(incidentservice.UpdateIncident(Incident));
    }

        @DeleteMapping("/Dashboard/Incident/delete/{id}")
    public void DeleteIncident(@PathVariable Long id) {
        incidentservice.DeleteIncident(id);
    }
}
