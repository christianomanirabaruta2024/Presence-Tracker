package com.upg.employee_management.service;

import com.example.employeemanagement.entity.Attendance;
import com.example.employeemanagement.entity.Employee;
import com.upg.employee_management.repository.AttendanceRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public Attendance checkIn(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        Attendance attendance = new Attendance();
        attendance.setEmployee(employee);
        attendance.setCheckIn(LocalDateTime.now());
        attendance.setDate(LocalDate.now());
        return attendanceRepository.save(attendance);
    }

    public Attendance checkOut(Long employeeId) {
        Attendance attendance = attendanceRepository.findByEmployeeIdAndDate(employeeId, LocalDate.now())
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No check-in found today"));
        attendance.setCheckOut(LocalDateTime.now());
        return attendanceRepository.save(attendance);
    }
}