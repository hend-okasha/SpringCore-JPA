package com.training.empmanager.notify.impl;

import com.training.empmanager.notify.Notifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class SmsNotifier implements Notifier {

    @Override
    public void send(String message) {
        System.out.println("SMS notification" + message);
    }
}
