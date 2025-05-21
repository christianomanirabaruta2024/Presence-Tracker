package com.upg.employee_management.repository;

import com.upg.employee_management.model.AttendanceRecord;
import com.upg.employee_management.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    Optional<AttendanceRecord> findByEmployeeAndCheckOutTimeIsNull(Employee employee);
    List<AttendanceRecord> findByEmployee(Employee employee);
}