package se.work.task.management.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import se.work.task.management.api.request.CreateTaskRequestDto;
import se.work.task.management.domain.exception.TaskNotFoundException;
import se.work.task.management.domain.exception.UnsupportedTaskOperation;
import se.work.task.management.domain.model.task.Task;
import se.work.task.management.domain.model.task.TaskFactory;
import se.work.task.management.domain.model.task.TaskType;
import se.work.task.management.domain.persistence.TaskRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static se.work.task.management.support.TestData.buildBasicTask;
import static se.work.task.management.support.TestData.getTestJwtClaims;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    private TaskService taskService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void beforeEach() {
        taskService = new TaskService(new TaskFactory(), taskRepository, applicationEventPublisher);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Nested
    class CreateTask {

        @Test
        void createTask() {
            when(authentication.getPrincipal()).thenReturn(getTestJwtClaims("use123"));
            var createRequest = CreateTaskRequestDto.builder()
                    .title("test title")
                    .build();
            when(taskRepository.save(any())).thenReturn(buildBasicTask("task01"));
            assertDoesNotThrow(() -> taskService.createTask(TaskType.BASIC, createRequest));
        }
    }

    @Nested
    class GetTask {

        @Test
        void shouldThrowExceptionWhenTaskNotFound() {
            assertThrowsExactly(TaskNotFoundException.class, () -> taskService.getTaskById("task01"));
        }

        @Test
        void shouldFetchTaskWhenFound() {
            String taskId = "task07";
            when(taskRepository.findById(taskId)).thenReturn(buildTask(taskId));
            var taskResponse = taskService.getTaskById(taskId);
            assertEquals("test title", taskResponse.title());
            assertEquals(TaskType.BASIC.name(), taskResponse.taskType());
        }
    }

    @Nested
    class DeleteTask {

        @Test
        void shouldThrowExceptionWhenUnauthorizedUserTriesToDelete() {
            when(authentication.getPrincipal()).thenReturn(getTestJwtClaims("use123"));
            when(taskRepository.findById(any())).thenReturn(buildTask("task01"));
            assertThrowsExactly(UnsupportedTaskOperation.class, () -> taskService.deleteTask("test-task"));
        }

        @Test
        void shouldDeleteWhenAuthorizedUserTriesToDelete() {
            when(authentication.getPrincipal()).thenReturn(getTestJwtClaims("tst001"));
            when(taskRepository.findById(any())).thenReturn(buildTask("task01"));
            assertDoesNotThrow(() -> taskService.deleteTask("test-task"));
        }

    }

    private Optional<Task> buildTask(String id) {
        return Optional.of(buildBasicTask(id));
    }

}