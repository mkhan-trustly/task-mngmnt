package se.work.task.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import se.work.task.events.TaskCreatedEvent;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {

    private final ReminderService reminderService;

    @EventListener
    public void handleTaskCreated(TaskCreatedEvent event) {
        log.info("A task({}) creation event has been received", event.taskId());
        reminderService.scheduleReminder(event.taskId(), event.assignedTo(), event.assignedBy());
    }
}
