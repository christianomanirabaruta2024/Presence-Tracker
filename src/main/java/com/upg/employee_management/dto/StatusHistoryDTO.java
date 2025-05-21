package com.upg.employee_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StatusHistoryDTO {
    private Long statusHistoryId;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotBlank(message = "Status is required")
    private String status;

    @NotNull(message = "Timestamp is required")
    private LocalDateTime timestamp;
}