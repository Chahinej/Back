package com.example.SystemAlerte.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SystemAlerte.Model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
