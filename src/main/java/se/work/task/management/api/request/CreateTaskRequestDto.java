package se.work.task.management.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record CreateTaskRequestDto(
        @NotNull
        @NotBlank
        String title,
        String description,
        Instant deadline,

        @Pattern(regexp = "^[a-zA-Z]{3}\\d{3}", message = "Field cannot contain special characters")
        String assigneeId,
        OptionsDto options) {

        public record OptionsDto(List<String> checklist) {
        }
}


