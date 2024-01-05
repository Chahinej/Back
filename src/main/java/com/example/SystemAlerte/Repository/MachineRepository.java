package com.example.SystemAlerte.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SystemAlerte.Model.Machine;

public interface MachineRepository extends JpaRepository<Machine, Long> {

    Machine findByName(String name);

    Machine findByType(String type);

    Machine findByFeature(String feature);

}
