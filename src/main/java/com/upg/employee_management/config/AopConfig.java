package com.upg.employee_management.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = false) // JDK proxies, matches your stack trace
public class AopConfig {
}