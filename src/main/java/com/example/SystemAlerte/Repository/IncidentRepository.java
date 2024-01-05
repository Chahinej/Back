package com.example.SystemAlerte.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SystemAlerte.Model.Incident;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    Incident findByName(String name);

    Incident findByType(String type);

}
