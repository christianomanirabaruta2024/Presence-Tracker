package com.example.employeemanagement.config;

import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.service.EmployeeService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeService employeeService;

    public CustomUserDetailsService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeService.findByEmail(username);
        if (employee == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        return new User(employee.getEmail(), employee.getPassword(),
                employee.getRole() == Employee.Role.MANAGER ?
                        java.util.Arrays.asList(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_MANAGER"),
                                new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_EMPLOYEE")) :
                        java.util.Arrays.asList(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_EMPLOYEE")));
    }
}