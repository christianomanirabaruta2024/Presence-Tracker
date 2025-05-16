package com.upg.employee_management.controller;

import com.example.employeemanagement.entity.Attendance;
import com.example.employeemanagement.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    @PostMapping("/check-in")
    public ResponseEntity<Attendance> checkIn() {
        Long employeeId = getCurrentEmployeeId();
        return ResponseEntity.ok(attendanceService.checkIn(employeeId));
    }

    @PostMapping("/check-out")
    public ResponseEntity<Attendance> checkOut() {
        Long employeeId = getCurrentEmployeeId();
        return ResponseEntity.ok(attendanceService.checkOut(employeeId));
    }

    private Long getCurrentEmployeeId() {
        return ((Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }
}