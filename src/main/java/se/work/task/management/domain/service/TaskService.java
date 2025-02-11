package se.work.task.management.domain.service;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import se.work.task.events.TaskCreatedEvent;
import se.work.task.management.api.request.CreateTaskRequestDto;
import se.work.task.management.api.response.TaskResponseDto;
import se.work.task.management.domain.exception.TaskNotFoundException;
import se.work.task.management.domain.model.task.*;
import se.work.task.management.domain.persistence.TaskRepository;

import static se.work.task.management.Utility.notNullThenGet;
import static se.work.task.management.api.mapper.TaskResponseMapper.mapTask;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskFactory taskFactory;
    private final TaskRepository taskRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @WithSpan
    public TaskResponseDto createTask(TaskType taskType, CreateTaskRequestDto request) {
        TaskStrategy taskStrategy = taskFactory.getTaskStrategy(taskType);
        Task task = taskStrategy.createTask(getTaskUser(), request);
        var savedTask = taskRepository.save(task);

        publishTaskCreatedEvent(savedTask);

        return mapTask(task);
    }

    private void publishTaskCreatedEvent(Task task) {
        var taskCreatedEvent = TaskCreatedEvent.builder()
                .taskId(task.getId())
                .assignedTo(notNullThenGet(task.getAssignee(), TaskUser::getUsername))
                .assignedBy(task.getCreatedBy().getUsername())
                .build();

        log.info("Dispatching event for the newly created task({})", task.getId());
        applicationEventPublisher.publishEvent(taskCreatedEvent);
    }

    public TaskResponseDto getTaskById(String taskId) {
        Task existingTask = findByIdOrThrowNotFound(taskId);
        return mapTask(existingTask);
    }

    public void deleteTask(String taskId) {
        Task existingTask = findByIdOrThrowNotFound(taskId);
        var currentUser = getTaskUser();
        log.info("User({}) requested to delete the task({}), validation to be performed", currentUser, taskId);
        existingTask.validateTaskDeletion(currentUser);
        taskRepository.delete(existingTask);
        log.info("Successfully deleted the task({})", taskId);
    }

    private Task findByIdOrThrowNotFound(String taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task by id %s not found.".formatted(taskId)));
    }

    private TaskUser getTaskUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return new TaskUser(jwt.getClaims());
        }
        throw new IllegalStateException("Expecting an authenticated user, no user found.");
    }
}
