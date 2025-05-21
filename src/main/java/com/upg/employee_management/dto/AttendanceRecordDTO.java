package com.upg.employee_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class AttendanceRecordDTO {
    private Long attendanceRecordId;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotBlank(message = "Status is required")
    private String status;

    private LocalTime checkInTime;

    private LocalTime checkOutTime;

    private String notes;

    private Long markedByEmployeeId;

    @NotNull(message = "Created at is required")
    private LocalDateTime createdAt;

    @NotNull(message = "Updated at is required")
    private LocalDateTime updatedAt;
}