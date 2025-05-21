package com.upg.employee_management.service;

import com.upg.employee_management.aop.Trackable;
import com.upg.employee_management.dto.EmployeeDTO;
import com.upg.employee_management.model.Employee;
import com.upg.employee_management.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Trackable
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        if (employeeRepository.findByUsername(employeeDTO.getUsername())!=null||
                employeeRepository.findByEmail(employeeDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Username or email already exists");
        }
        Employee employee = new Employee();
        employee.setUsername(employeeDTO.getUsername());
        employee.setEmail(employeeDTO.getEmail());
        employee.setPasswordHash(passwordEncoder.encode("defaultPassword123"));
        employee.setAvatarUrl(employeeDTO.getAvatarUrl());
        employee.setDepartment(employeeDTO.getDepartment());
        employee.setRole(employeeDTO.getRole());
        employee.setJoiningDate(employeeDTO.getJoiningDate());
        employee.setAdmin(employeeDTO.getIsAdmin());
        employee.setCurrentStatus(Employee.Status.valueOf(employeeDTO.getCurrentStatus()));
        employee.setLastStatusUpdate(employeeDTO.getLastStatusUpdate());
        employee.setBio(employeeDTO.getBio());
        employee.setCreatedAt(employeeDTO.getCreatedAt());
        employee.setUpdatedAt(employeeDTO.getUpdatedAt());
        employee = employeeRepository.save(employee);
        employeeDTO.setEmployeeId(employee.getEmployeeId());
        return employeeDTO;
    }

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<EmployeeDTO> getEmployeeById(Long id) {
        return employeeRepository.findById(id).map(this::convertToDTO);
    }

    public Employee getEmployeeByUsername(String username) {
        return employeeRepository.findByUsername(username);
    }

    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        if (!employee.getUsername().equals(employeeDTO.getUsername()) &&
                employeeRepository.findByUsername(employeeDTO.getUsername())!=null) {
            throw new RuntimeException("Username already exists");
        }
        if (!employee.getEmail().equals(employeeDTO.getEmail()) &&
                employeeRepository.findByEmail(employeeDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        employee.setUsername(employeeDTO.getUsername());
        employee.setEmail(employeeDTO.getEmail());
        employee.setAvatarUrl(employeeDTO.getAvatarUrl());
        employee.setDepartment(employeeDTO.getDepartment());
        employee.setRole(employeeDTO.getRole());
        employee.setJoiningDate(employeeDTO.getJoiningDate());
        employee.setAdmin(employeeDTO.getIsAdmin());
        employee.setCurrentStatus(Employee.Status.valueOf(employeeDTO.getCurrentStatus()));
        employee.setLastStatusUpdate(employeeDTO.getLastStatusUpdate());
        employee.setBio(employeeDTO.getBio());
        employee.setUpdatedAt(employeeDTO.getUpdatedAt());
        employee = employeeRepository.save(employee);
        return convertToDTO(employee);
    }

    public Optional<Employee> findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setUsername(employee.getUsername());
        dto.setEmail(employee.getEmail());
        dto.setAvatarUrl(employee.getAvatarUrl());
        dto.setDepartment(employee.getDepartment());
        dto.setRole(employee.getRole());
        dto.setJoiningDate(employee.getJoiningDate());
        dto.setIsAdmin(employee.isAdmin());
        dto.setCurrentStatus(employee.getCurrentStatus().name());
        dto.setLastStatusUpdate(employee.getLastStatusUpdate());
        dto.setBio(employee.getBio());
        dto.setCreatedAt(employee.getCreatedAt());
        dto.setUpdatedAt(employee.getUpdatedAt());
        return dto;
    }


}