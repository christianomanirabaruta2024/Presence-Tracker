package com.upg.employee_management.service;

import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void testFindById() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee result = employeeService.findById(1L);
        assertEquals("John", result.getFirstName());
    }
}