package com.upg.employee_management.controller;

import com.upg.employee_management.dto.LeaveRequestDTO;
import com.upg.employee_management.dto.StatusRequest;
import com.upg.employee_management.model.Employee;
import com.upg.employee_management.model.LeaveRequest;
import com.upg.employee_management.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave")
public class LeaveRequestController {
    @Autowired
    private LeaveRequestService leaveRequestService;

    @PostMapping("/request")
    public ResponseEntity<?> createLeaveRequest(@AuthenticationPrincipal Employee employee, @RequestBody LeaveRequestDTO leaveRequestDTO) {
        leaveRequestDTO.setEmployeeId(employee.getEmployeeId());
        leaveRequestDTO.setStatus(LeaveRequest.Status.PENDING.name());
        LeaveRequestDTO savedRequest = leaveRequestService.createLeaveRequest(leaveRequestDTO);
        return ResponseEntity.ok("Leave request created for " + employee.getUsername());
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateLeaveRequestStatus(@AuthenticationPrincipal Employee employee, @PathVariable Long id, @RequestBody StatusRequest statusRequest) {
        LeaveRequestDTO updatedRequest = leaveRequestService.updateLeaveRequestStatus(id, statusRequest.getStatus(), employee.getUsername());
        return ResponseEntity.ok("Leave request status updated to " + statusRequest.getStatus());
    }

    @GetMapping("/requests")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllLeaveRequests() {
        List<LeaveRequestDTO> requests = leaveRequestService.getAllLeaveRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/my-requests")
    public ResponseEntity<?> getMyLeaveRequests(@AuthenticationPrincipal Employee employee, @RequestParam(required = false) String status) {
        List<LeaveRequestDTO> requests = leaveRequestService.getLeaveRequestsByEmployeeAndStatus(employee.getEmployeeId(), status);
        return ResponseEntity.ok(requests);
    }
}
