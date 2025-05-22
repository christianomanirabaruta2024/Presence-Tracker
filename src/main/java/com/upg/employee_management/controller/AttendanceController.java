package com.upg.employee_management.controller;

import com.upg.employee_management.dto.AttendanceRecordDTO;
import com.upg.employee_management.dto.StatusRequest;
import com.upg.employee_management.model.Employee;
import com.upg.employee_management.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/check-in")
    public ResponseEntity<?> checkIn(@AuthenticationPrincipal Employee employee) {
        AttendanceRecordDTO record = attendanceService.checkIn(employee.getEmployeeId());
        return ResponseEntity.ok("Check-in recorded for " + employee.getUsername());
    }

    @PostMapping("/check-out")
    public ResponseEntity<?> checkOut(@AuthenticationPrincipal Employee employee) {
        AttendanceRecordDTO record = attendanceService.checkOut(employee.getEmployeeId());
        return ResponseEntity.ok("Check-out recorded for " + employee.getUsername());
    }

    @PostMapping("/status")
    public ResponseEntity<?> updateStatus(@AuthenticationPrincipal Employee employee, @RequestBody StatusRequest statusRequest) {
        AttendanceRecordDTO record = attendanceService.updateStatus(employee.getEmployeeId(), statusRequest.getStatus());
        return ResponseEntity.ok("Status updated to " + statusRequest.getStatus() + " for " + employee.getUsername());
    }

    @PostMapping("/record")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createAttendanceRecord(@RequestBody AttendanceRecordDTO attendanceRecordDTO) {
        AttendanceRecordDTO savedRecord = attendanceService.createAttendanceRecord(attendanceRecordDTO);
        return ResponseEntity.ok(savedRecord);
    }

    @GetMapping("/records")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllAttendanceRecords() {
        List<AttendanceRecordDTO> records = attendanceService.getAllAttendanceRecords();
        return ResponseEntity.ok(records);
    }

    @GetMapping("/my-records")
    public ResponseEntity<?> getMyAttendanceRecords(@AuthenticationPrincipal Employee employee) {
        List<AttendanceRecordDTO> records = attendanceService.getAttendanceRecordsByEmployee(employee.getEmployeeId());
        return ResponseEntity.ok(records);
    }

}

