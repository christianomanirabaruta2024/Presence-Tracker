package com.upg.employee_management.service;

import com.upg.employee_management.aop.Trackable;
import com.upg.employee_management.dto.LeaveRequestDTO;
import com.upg.employee_management.model.Employee;
import com.upg.employee_management.model.LeaveRequest;
import com.upg.employee_management.repository.EmployeeRepository;
import com.upg.employee_management.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Trackable
@Service
@RequiredArgsConstructor
public class LeaveRequestService {
    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveRequestDTO createLeaveRequest(LeaveRequestDTO leaveRequestDTO) {
//        if (HolidayRepository.existsByDateBetween(leaveRequestDTO.getStartDate(), leaveRequestDTO.getEndDate())) {
//            throw new RuntimeException("Leave request overlaps with a holiday");
//        }
        Employee employee = employeeRepository.findById(leaveRequestDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployee(employee);
        leaveRequest.setLeaveType(LeaveRequest.LeaveType.valueOf(leaveRequestDTO.getLeaveType()));
        leaveRequest.setStartDate(leaveRequestDTO.getStartDate());
        leaveRequest.setEndDate(leaveRequestDTO.getEndDate());
        leaveRequest.setReason(leaveRequestDTO.getReason());
        leaveRequest.setStatus(LeaveRequest.Status.PENDING); // Default to PENDING
        leaveRequest.setRequestedDate(leaveRequestDTO.getRequestedDate() != null ? leaveRequestDTO.getRequestedDate() : LocalDateTime.now());
        if (leaveRequestDTO.getActionedByEmployeeId() != null) {
            Employee actionedBy = employeeRepository.findById(leaveRequestDTO.getActionedByEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Actioned by employee not found"));
            leaveRequest.setActionedBy(actionedBy);
        }
        leaveRequest.setActionedDate(leaveRequestDTO.getActionedDate());
        leaveRequest.setCreatedAt(leaveRequestDTO.getCreatedAt() != null ? leaveRequestDTO.getCreatedAt() : LocalDateTime.now());
        leaveRequest.setUpdatedAt(leaveRequestDTO.getUpdatedAt() != null ? leaveRequestDTO.getUpdatedAt() : LocalDateTime.now());
        leaveRequest = leaveRequestRepository.save(leaveRequest);
        leaveRequestDTO.setLeaveRequestId(leaveRequest.getLeaveRequestId());
        return leaveRequestDTO;
    }

    public LeaveRequestDTO updateLeaveRequestStatus(Long id, String status, String adminUsername) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
        Employee admin = employeeRepository.findByUsername(adminUsername);
        if (!admin.isAdmin()) {
            throw new RuntimeException("User is not an admin");
        }
        leaveRequest.setStatus(LeaveRequest.Status.valueOf(status));
        leaveRequest.setActionedBy(admin);
        leaveRequest.setActionedDate(LocalDateTime.now());
        leaveRequest.setUpdatedAt(LocalDateTime.now());
        leaveRequest = leaveRequestRepository.save(leaveRequest);
        return convertToDTO(leaveRequest);
    }

    public List<LeaveRequestDTO> getAllLeaveRequests() {
        return leaveRequestRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<LeaveRequestDTO> getLeaveRequestsByEmployeeAndStatus(Long employeeId, String status) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        LeaveRequest.Status leaveStatus = status != null ? LeaveRequest.Status.valueOf(status) : null;
        List<LeaveRequest> requests = leaveStatus != null
                ? leaveRequestRepository.findByEmployeeAndStatus(employee, leaveStatus)
                : leaveRequestRepository.findAll().stream()
                .filter(lr -> lr.getEmployee().getEmployeeId().equals(employeeId))
                .collect(Collectors.toList());
        return requests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public boolean isOnApprovedLeave(Long employeeId, LocalDate date) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return leaveRequestRepository.findByEmployeeAndStatus(employee, LeaveRequest.Status.APPROVED).stream()
                .anyMatch(lr -> !date.isBefore(lr.getStartDate()) && !date.isAfter(lr.getEndDate()));
    }

    private LeaveRequestDTO convertToDTO(LeaveRequest leaveRequest) {
        LeaveRequestDTO dto = new LeaveRequestDTO();
        dto.setLeaveRequestId(leaveRequest.getLeaveRequestId());
        dto.setEmployeeId(leaveRequest.getEmployee().getEmployeeId());
        dto.setLeaveType(leaveRequest.getLeaveType().name());
        dto.setStartDate(leaveRequest.getStartDate());
        dto.setEndDate(leaveRequest.getEndDate());
        dto.setReason(leaveRequest.getReason());
        dto.setStatus(leaveRequest.getStatus().name());
        dto.setRequestedDate(leaveRequest.getRequestedDate());
        dto.setActionedByEmployeeId(leaveRequest.getActionedBy() != null ? leaveRequest.getActionedBy().getEmployeeId() : null);
        dto.setActionedDate(leaveRequest.getActionedDate());
        dto.setCreatedAt(leaveRequest.getCreatedAt());
        dto.setUpdatedAt(leaveRequest.getUpdatedAt());
        return dto;
    }
}