package com.training.empmanager.config;

import com.training.empmanager.notify.NotificationManager;
import com.training.empmanager.notify.Notifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@ComponentScan(basePackages = "com.training.empmanager")
@PropertySource("application.properties")
public class AppConfig {
    @Bean
    public NotificationManager notificationManager(List<Notifier> notifiers) {
        return new NotificationManager(notifiers);
    }
}
