package com.upg.employee_management.controller;

import com.upg.employee_management.dto.AuthRequest;
import com.upg.employee_management.dto.AuthResponse;
import com.upg.employee_management.dto.EmployeeDTO;
import com.upg.employee_management.model.Employee;
import com.upg.employee_management.service.EmployeeService;
import com.upg.employee_management.util.JwtUtil;
import com.upg.employee_management.service.EmployeeDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmployeeDetailsService employeeDetailsService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                String token = jwtUtil.generateToken(authRequest.getUsername());

                Employee employee=employeeService.getEmployeeByUsername(authRequest.getUsername());
                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("employee", employee);



                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Authentication failed."));
            }

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password."));
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "User account is disabled."));
        } catch (LockedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "User account is locked."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred during authentication."));
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody EmployeeDTO employee) {
        EmployeeDTO savedEmployee = employeeService.createEmployee(employee);
        return ResponseEntity.ok("Employee registered: " + savedEmployee.getUsername());
    }
}


