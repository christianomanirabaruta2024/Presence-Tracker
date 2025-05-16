package com.upg.employee_management.repository;

import com.example.employeemanagement.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}