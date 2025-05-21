package com.upg.employee_management.service;

import com.upg.employee_management.aop.Trackable;
import com.upg.employee_management.dto.AttendanceRecordDTO;
import com.upg.employee_management.model.AttendanceRecord;
import com.upg.employee_management.model.Employee;
import com.upg.employee_management.repository.AttendanceRecordRepository;
import com.upg.employee_management.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Trackable
@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveRequestService leaveRequestService;

    public AttendanceRecordDTO checkIn(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (leaveRequestService.isOnApprovedLeave(employeeId, LocalDate.now())) {
            throw new RuntimeException("Cannot check in while on approved leave");
        }

        attendanceRecordRepository.findByEmployeeAndCheckOutTimeIsNull(employee)
                .ifPresent(record -> {
                    throw new RuntimeException("Employee already checked in");
                });

        AttendanceRecord attendanceRecord = new AttendanceRecord();
        attendanceRecord.setEmployee(employee);
        attendanceRecord.setDate(LocalDate.now());
        attendanceRecord.setStatus(AttendanceRecord.Status.PRESENT);
        attendanceRecord.setCheckInTime(LocalTime.from(LocalDateTime.now()));
        attendanceRecord = attendanceRecordRepository.save(attendanceRecord);

        employee.setCurrentStatus(Employee.Status.ONLINE);
        employee.setLastStatusUpdate(LocalDateTime.now());
        employeeRepository.save(employee);

        return convertToDTO(attendanceRecord);
    }

    public AttendanceRecordDTO checkOut(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        AttendanceRecord attendanceRecord = attendanceRecordRepository.findByEmployeeAndCheckOutTimeIsNull(employee)
                .orElseThrow(() -> new RuntimeException("No active check-in found"));

        attendanceRecord.setCheckOutTime(LocalTime.from(LocalDateTime.now()));
        attendanceRecord.setStatus(AttendanceRecord.Status.ABSENT);
        attendanceRecord.setUpdatedAt(LocalDateTime.now());
        attendanceRecord = attendanceRecordRepository.save(attendanceRecord);

        employee.setCurrentStatus(Employee.Status.OFFLINE);
        employee.setLastStatusUpdate(LocalDateTime.now());
        employeeRepository.save(employee);

        return convertToDTO(attendanceRecord);
    }

    public AttendanceRecordDTO updateStatus(Long employeeId, String status) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (leaveRequestService.isOnApprovedLeave(employeeId, LocalDate.now())) {
            throw new RuntimeException("Cannot update status while on approved leave");
        }

        AttendanceRecord attendanceRecord = new AttendanceRecord();
        attendanceRecord.setEmployee(employee);
        attendanceRecord.setDate(LocalDate.now());
        attendanceRecord.setStatus(AttendanceRecord.Status.valueOf(status));
        attendanceRecord = attendanceRecordRepository.save(attendanceRecord);

        employee.setCurrentStatus(Employee.Status.valueOf(status));
        employee.setLastStatusUpdate(LocalDateTime.now());
        employeeRepository.save(employee);

        return convertToDTO(attendanceRecord);
    }

    public AttendanceRecordDTO createAttendanceRecord(AttendanceRecordDTO attendanceRecordDTO) {
        Employee employee = employeeRepository.findById(attendanceRecordDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        if (leaveRequestService.isOnApprovedLeave(employee.getEmployeeId(), attendanceRecordDTO.getDate())) {
            throw new RuntimeException("Cannot create attendance record while on approved leave");
        }
        AttendanceRecord attendanceRecord = new AttendanceRecord();
        attendanceRecord.setEmployee(employee);
        attendanceRecord.setDate(attendanceRecordDTO.getDate());
        attendanceRecord.setStatus(AttendanceRecord.Status.valueOf(attendanceRecordDTO.getStatus()));
        attendanceRecord.setCheckInTime(attendanceRecordDTO.getCheckInTime());
        attendanceRecord.setCheckOutTime(attendanceRecordDTO.getCheckOutTime());
        attendanceRecord.setNotes(attendanceRecordDTO.getNotes());
        if (attendanceRecordDTO.getMarkedByEmployeeId() != null) {
            Employee markedBy = employeeRepository.findById(attendanceRecordDTO.getMarkedByEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Marked by employee not found"));
            attendanceRecord.setMarkedBy(markedBy);
        }
        attendanceRecord.setCreatedAt(attendanceRecordDTO.getCreatedAt() != null ? attendanceRecordDTO.getCreatedAt() : LocalDateTime.now());
        attendanceRecord.setUpdatedAt(attendanceRecordDTO.getUpdatedAt() != null ? attendanceRecordDTO.getUpdatedAt() : LocalDateTime.now());
        attendanceRecord = attendanceRecordRepository.save(attendanceRecord);
        attendanceRecordDTO.setAttendanceRecordId(attendanceRecord.getAttendanceRecordId());
        return attendanceRecordDTO;
    }

    public List<AttendanceRecordDTO> getAllAttendanceRecords() {
        return attendanceRecordRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AttendanceRecordDTO> getAttendanceRecordsByEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return attendanceRecordRepository.findByEmployee(employee).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public void markLeaveAttendance(Long employeeId, LocalDate date) {
        if (leaveRequestService.isOnApprovedLeave(employeeId, date)) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            AttendanceRecord attendanceRecord = new AttendanceRecord();
            attendanceRecord.setEmployee(employee);
            attendanceRecord.setDate(date);
            attendanceRecord.setStatus(AttendanceRecord.Status.ON_LEAVE);
            attendanceRecordRepository.save(attendanceRecord);
        }
    }
    private AttendanceRecordDTO convertToDTO(AttendanceRecord attendanceRecord) {
        AttendanceRecordDTO dto = new AttendanceRecordDTO();
        dto.setAttendanceRecordId(attendanceRecord.getAttendanceRecordId());
        dto.setEmployeeId(attendanceRecord.getEmployee().getEmployeeId());
        dto.setDate(attendanceRecord.getDate());
        dto.setStatus(attendanceRecord.getStatus().name());
        dto.setCheckInTime(attendanceRecord.getCheckInTime());
        dto.setCheckOutTime(attendanceRecord.getCheckOutTime());
        dto.setNotes(attendanceRecord.getNotes());
        dto.setMarkedByEmployeeId(attendanceRecord.getMarkedBy() != null ? attendanceRecord.getMarkedBy().getEmployeeId() : null);
        dto.setCreatedAt(attendanceRecord.getCreatedAt());
        dto.setUpdatedAt(attendanceRecord.getUpdatedAt());
        return dto;
    }
}