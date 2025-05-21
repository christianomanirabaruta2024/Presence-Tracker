package com.upg.employee_management.repository;

import com.upg.employee_management.model.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long> {
    // Find all status history records for a specific employee
    List<StatusHistory> findByEmployeeEmployeeId(Long employeeId);

    // Find status history records for an employee within a date range
    List<StatusHistory> findByEmployeeEmployeeIdAndTimestampBetween(
            Long employeeId, LocalDateTime start, LocalDateTime end);
}