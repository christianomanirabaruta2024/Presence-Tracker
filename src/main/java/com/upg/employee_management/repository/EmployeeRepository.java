package com.upg.employee_management.repository;

import com.upg.employee_management.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByUsername(String username);
    Optional<Employee> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}