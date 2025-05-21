package com.upg.employee_management.service;

import com.upg.employee_management.aop.Trackable;
import com.upg.employee_management.dto.StatusHistoryDTO;
import com.upg.employee_management.model.Employee;
import com.upg.employee_management.model.StatusHistory;
import com.upg.employee_management.repository.EmployeeRepository;
import com.upg.employee_management.repository.StatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Trackable
@Service
@RequiredArgsConstructor
public class StatusHistoryService {

    private final StatusHistoryRepository statusHistoryRepository;
    private final EmployeeRepository employeeRepository;

    public StatusHistoryDTO createStatusHistory(StatusHistoryDTO statusHistoryDTO) {
        Employee employee = employeeRepository.findById(statusHistoryDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        StatusHistory statusHistory = new StatusHistory();
        statusHistory.setEmployee(employee);
        statusHistory.setStatus(Employee.Status.valueOf(statusHistoryDTO.getStatus()));
        statusHistory.setTimestamp(statusHistoryDTO.getTimestamp());
        statusHistory = statusHistoryRepository.save(statusHistory);
        statusHistoryDTO.setStatusHistoryId(statusHistory.getStatusHistoryId());
        return statusHistoryDTO;
    }

    public List<StatusHistoryDTO> getAllStatusHistories() {
        return statusHistoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<StatusHistoryDTO> getStatusHistoriesByEmployee(Long employeeId) {
        return statusHistoryRepository.findByEmployeeEmployeeId(employeeId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<StatusHistoryDTO> getStatusHistoriesByEmployeeAndDateRange(
            Long employeeId, LocalDateTime start, LocalDateTime end) {
        return statusHistoryRepository.findByEmployeeEmployeeIdAndTimestampBetween(employeeId, start, end).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private StatusHistoryDTO convertToDTO(StatusHistory statusHistory) {
        StatusHistoryDTO dto = new StatusHistoryDTO();
        dto.setStatusHistoryId(statusHistory.getStatusHistoryId());
        dto.setEmployeeId(statusHistory.getEmployee().getEmployeeId());
        dto.setStatus(statusHistory.getStatus().name());
        dto.setTimestamp(statusHistory.getTimestamp());
        return dto;
    }
}