package com.upg.employee_management.controller;

import com.upg.employee_management.dto.StatusHistoryDTO;
import com.upg.employee_management.service.StatusHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/status-history")
@RequiredArgsConstructor
public class StatusHistoryController {

    private final StatusHistoryService statusHistoryService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<StatusHistoryDTO> createStatusHistory(@Valid @RequestBody StatusHistoryDTO statusHistoryDTO) {
        return ResponseEntity.ok(statusHistoryService.createStatusHistory(statusHistoryDTO));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<StatusHistoryDTO>> getAllStatusHistories() {
        return ResponseEntity.ok(statusHistoryService.getAllStatusHistories());
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<StatusHistoryDTO>> getStatusHistoriesByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(statusHistoryService.getStatusHistoriesByEmployee(employeeId));
    }

    @GetMapping("/employee/{employeeId}/range")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<StatusHistoryDTO>> getStatusHistoriesByEmployeeAndDateRange(
            @PathVariable Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(statusHistoryService.getStatusHistoriesByEmployeeAndDateRange(employeeId, start, end));
    }
}