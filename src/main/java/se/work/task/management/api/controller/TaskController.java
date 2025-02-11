package se.work.task.management.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.work.task.management.api.request.CreateTaskRequestDto;
import se.work.task.management.api.response.TaskResponseDto;
import se.work.task.management.domain.model.task.TaskType;
import se.work.task.management.domain.service.TaskService;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@RequestParam TaskType taskType,
                                                      @Valid @RequestBody CreateTaskRequestDto request) {
        TaskResponseDto taskResponse = taskService.createTask(taskType, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(taskResponse);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable String taskId) {
        TaskResponseDto taskResponse = taskService.getTaskById(taskId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(taskResponse);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable String taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

}
