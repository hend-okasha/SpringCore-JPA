package com.training.empmanager.audit;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Scope("prototype")
public class AuditLogger {

    @PostConstruct
    public void init(){
        System.out.println("[AuditLogger " + "] created at " + LocalDateTime.now());
    }
    public void log(String message) {
        System.out.println("[AuditLogger "  + "] " + LocalDateTime.now() + " - " + message);
    }

    @PreDestroy
    public void destroy() {
        System.out.println("[AuditLogger "  + "] destroyed at " + LocalDateTime.now());
    }

}
