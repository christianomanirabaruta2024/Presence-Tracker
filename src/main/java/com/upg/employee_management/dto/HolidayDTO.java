package com.upg.employee_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class HolidayDTO {
    private Long holidayId;

    @NotBlank(message = "Holiday name is required")
    private String name;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotBlank(message = "Type is required")
    private String type;

    @NotNull(message = "Created at is required")
    private LocalDateTime createdAt;

    @NotNull(message = "Updated at is required")
    private LocalDateTime updatedAt;
}