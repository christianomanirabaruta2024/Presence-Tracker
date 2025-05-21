package com.upg.employee_management.repository;

import com.upg.employee_management.model.Employee;
import com.upg.employee_management.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployeeAndStatus(Employee employee, LeaveRequest.Status status);
}