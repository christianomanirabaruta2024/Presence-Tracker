package com.upg.employee_management.service;

import com.upg.employee_management.aop.Trackable;
import com.upg.employee_management.model.Employee;
import com.upg.employee_management.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Trackable
@Service
public class EmployeeDetailsService implements UserDetailsService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByUsername(username);
        if (employee==null) {
            throw new UsernameNotFoundException("Employee not found with username: " + username);
        }
        return employee;
    }
}