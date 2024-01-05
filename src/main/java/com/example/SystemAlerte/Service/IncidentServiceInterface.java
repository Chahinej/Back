package com.example.SystemAlerte.Service;

import java.util.List;

import com.example.SystemAlerte.Model.Incident;

public interface IncidentServiceInterface {
    Incident SaveIncident(Incident Incident);

    Incident getIncident(String name);

    List<Incident> getIncidents();

    Incident UpdateIncident(Incident Incident);

    void DeleteIncident(Long id);
}
