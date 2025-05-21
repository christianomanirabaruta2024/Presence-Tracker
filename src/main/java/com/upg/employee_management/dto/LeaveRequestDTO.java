package com.upg.employee_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class LeaveRequestDTO {
    private Long leaveRequestId;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotBlank(message = "Leave type is required")
    private String leaveType;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private String reason;

    @NotBlank(message = "Status is required")
    private String status;

    @NotNull(message = "Requested date is required")
    private LocalDateTime requestedDate;

    private Long actionedByEmployeeId;

    private LocalDateTime actionedDate;

    @NotNull(message = "Created at is required")
    private LocalDateTime createdAt;

    @NotNull(message = "Updated at is required")
    private LocalDateTime updatedAt;
}