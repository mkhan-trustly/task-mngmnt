package se.work.task.management.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import se.work.task.management.api.request.CreateTaskRequestDto;
import se.work.task.management.domain.model.task.Task;
import se.work.task.management.domain.model.task.TaskType;
import se.work.task.management.domain.model.task.TaskUser;
import se.work.task.management.domain.persistence.BasicTask;
import se.work.task.management.domain.persistence.TaskRepository;
import se.work.task.management.domain.service.TaskService;

import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.work.task.management.support.TestData.buildBasicTask;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class CreateTask {

        @ParameterizedTest
        @MethodSource("invalidRequests")
        @WithMockUser(username = "testUser")
        void shouldThrowBadRequestOnSendingInvalidRequests(String type, CreateTaskRequestDto request, String expectedBody) throws Exception {
            mockMvc.perform(
                            post("/api/v1/tasks?taskType=" + type)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(request))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().string(expectedBody));
        }

        private static Stream<Arguments> invalidRequests() {
            return Stream.of(
                    Arguments.of(
                            null, null, "No enum constant se.work.task.management.domain.model.task.TaskType.null"
                    ),
                    Arguments.of(
                            "UNKNOWN", null, "No enum constant se.work.task.management.domain.model.task.TaskType.UNKNOWN"
                    ),
                    Arguments.of(
                            TaskType.BASIC.name(),
                            CreateTaskRequestDto.builder().build(),
                            "Field title: must not be blank. Field title: must not be null"
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("validRequests")
        void shouldCreateTaskOnSendingValidRequests(String type, CreateTaskRequestDto request, String expectedBody) throws Exception {
            var token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImthYTAwNyIsIm5hbWUiOiJKb2huIERvZSIsImVtYWlsSWQiOiJra2FAZ21haWwuY29tIn0.foqBEDUQAw7Psum7ES4EdFvHwbHoLbqIb_THXPmZaWE";
            when(taskRepository.save(any())).thenReturn(buildBasicTask("task=1"));
            mockMvc.perform(
                            post("/api/v1/tasks?taskType=" + type)
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(request))
                    )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.title").value(expectedBody));
        }

        private static Stream<Arguments> validRequests() {
            return Stream.of(
                    Arguments.of(
                            TaskType.BASIC.name(),
                            CreateTaskRequestDto.builder()
                                    .title("test title")
                                    .build(),
                            "test title"
                    )
            );
        }

    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}