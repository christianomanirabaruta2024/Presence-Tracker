package com.upg.employee_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class EmployeeDTO {
    private Long employeeId;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    private String avatarUrl;

    private String department;

    private String role;

    @NotNull(message = "Joining date is required")
    private LocalDate joiningDate;

    @NotNull(message = "Admin status is required")
    private Boolean isAdmin;

    @NotBlank(message = "Current status is required")
    private String currentStatus;

    private LocalDateTime lastStatusUpdate;

    private String bio;

    @NotNull(message = "Created at is required")
    private LocalDateTime createdAt;

    @NotNull(message = "Updated at is required")
    private LocalDateTime updatedAt;
}