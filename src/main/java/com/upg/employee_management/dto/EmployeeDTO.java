package com.upg.employee_management.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class EmployeeDTO {
    private Long employeeId;

    private String username;
    private String email;

    private String avatarUrl;

    private String department;

    private String role;

    private LocalDate joiningDate;

    private Boolean isAdmin=false;
    private String currentStatus;

    private LocalDateTime lastStatusUpdate;

    private String bio;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}