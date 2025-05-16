package com.upg.employee_management.repository;

import com.example.employeemanagement.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByEmployeeIdAndDate(Long employeeId, LocalDate date);
}