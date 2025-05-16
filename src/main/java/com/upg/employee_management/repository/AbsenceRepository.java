package com.upg.employee_management.repository;

import com.example.employeemanagement.entity.Absence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbsenceRepository extends JpaRepository<Absence, Long> {
    long countByEmployeeIdAndNotifiedFalse(Long employeeId);
    List<Absence> findByEmployeeIdAndNotifiedFalse(Long employeeId);
}