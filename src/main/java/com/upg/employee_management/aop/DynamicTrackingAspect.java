package com.upg.employee_management.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class DynamicTrackingAspect {
    private static final Logger logger = LoggerFactory.getLogger(DynamicTrackingAspect.class);

    @Value("${aop.tracking.enabled:true}")
    private boolean trackingEnabled;

    @Pointcut("@annotation(com.upg.employee_management.aop.Trackable) || @within(com.upg.employee_management.aop.Trackable)")
    public void trackableMethods() {}

    @Around("trackableMethods()")
    public Object trackExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!trackingEnabled) {
            return joinPoint.proceed();
        }
        String methodName = joinPoint.getSignature().toShortString();
        logger.info("Entering method: {}", methodName);
        try {
            Object result = joinPoint.proceed();
            logger.info("Exiting method: {}", methodName);
            return result;
        } catch (Throwable t) {
            logger.error("Error in method {}: {}", methodName, t.getMessage());
            throw t;
        }
    }
}