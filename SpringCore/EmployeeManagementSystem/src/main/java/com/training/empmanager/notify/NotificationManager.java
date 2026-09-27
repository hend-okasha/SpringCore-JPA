package com.training.empmanager.notify;

import org.springframework.stereotype.Component;

import java.util.List;


public class NotificationManager {

    private List<Notifier> notifiers;

    public NotificationManager(List<Notifier> notifiers){
        this.notifiers = notifiers;
    }

    public void notifyAll(String message) {

        System.out.println(
                "NotificationManager: sending notification..."
        );

        for (Notifier notifier : notifiers) {
            notifier.send(message);
        }
    }

}
