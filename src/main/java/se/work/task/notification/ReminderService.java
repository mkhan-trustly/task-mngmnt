package se.work.task.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReminderService {

    public void scheduleReminder(String taskId, String assignedTo, String assignedBy) {
        log.info("A reminder is set to notify assignee about an upcoming Task!");
    }
}
