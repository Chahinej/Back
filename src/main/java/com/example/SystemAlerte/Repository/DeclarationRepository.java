package com.example.SystemAlerte.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.SystemAlerte.Model.DeclarationOfIncident;

import java.util.List;

public interface DeclarationRepository extends JpaRepository<DeclarationOfIncident, Long> {
    DeclarationOfIncident findByName(String name);

    @Query("SELECT d FROM DeclarationOfIncident d WHERE d.id_user = id_user ")
    List<DeclarationOfIncident> findByUser(Long id_user);

}
