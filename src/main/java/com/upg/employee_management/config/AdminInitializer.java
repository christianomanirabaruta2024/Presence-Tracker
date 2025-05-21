package com.upg.employee_management.config;

import com.upg.employee_management.model.Employee;
import com.upg.employee_management.model.Employee.Status;
import com.upg.employee_management.repository.EmployeeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class AdminInitializer {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void createAdminUser() {
        String adminEmail = "admin@upg.com";

        if (employeeRepository.findByEmail(adminEmail).isEmpty()) {
            Employee admin = new Employee();
            admin.setUsername("admin");
            admin.setEmail(adminEmail);
            admin.setPasswordHash(passwordEncoder.encode("admin123")); // Replace with secure password
            admin.setAvatarUrl(null);
            admin.setDepartment("Administration");
            admin.setRole("Administrator");
            admin.setJoiningDate(LocalDate.now());
            admin.setAdmin(true);
            admin.setCurrentStatus(Status.ONLINE);
            admin.setLastStatusUpdate(LocalDateTime.now());
            admin.setBio("System administrator");
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());

            employeeRepository.save(admin);
            System.out.println("✅ Admin user created.");
            System.out.println(admin.toString());
        }
    }
}
