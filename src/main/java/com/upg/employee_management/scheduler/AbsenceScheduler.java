//package com.upg.employee_management.scheduler;
//
//import com.upg.employee_management.service.AttendanceService;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//public class AbsenceScheduler {
//    private final AttendanceService attendanceService;
//
//    public AbsenceScheduler(AttendanceService attendanceService) {
//        this.attendanceService = attendanceService;
//    }
//
//    @Scheduled(cron = "0 0 0 * * ?") // Run daily at midnight
//    public void checkAbsences() {
//        attendanceService.checkForTwoDayAbsences();
//    }
//}