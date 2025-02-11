package se.work.task.management.api.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class CreateTaskRequestDtoTest {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    @ParameterizedTest
    @MethodSource("invalidCreateTaskRequest")
    void shouldReportViolationsOnInvalidCreateTaskRequest(List<String> expectedConstraints, CreateTaskRequestDto request) {
        var violationList = validateRequest(request);

        assertIterableEquals(expectedConstraints, violationList);
    }

    private static Stream<Arguments> invalidCreateTaskRequest() {
        return Stream.of(
                Arguments.of(
                        List.of("must not be blank", "must not be null"), CreateTaskRequestDto.builder()
                                .build()
                ),
                Arguments.of(
                        List.of("Field cannot contain special characters"), CreateTaskRequestDto.builder()
                                .title("test title")
                                        .assigneeId("äöå")
                                .build()
                ),
                Arguments.of(
                        List.of("Field cannot contain special characters"), CreateTaskRequestDto.builder()
                                .title("test title")
                                .assigneeId("kam-01")
                                .build()
                ),
                Arguments.of(
                        List.of("Field cannot contain special characters"), CreateTaskRequestDto.builder()
                                .title("test title")
                                .assigneeId("kaa01")
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("validCreateTaskRequest")
    void shouldReportNoViolationsOnValidCreateTaskRequest(List<String> expectedConstraints, CreateTaskRequestDto request) {
        var violationList = validateRequest(request);

        assertIterableEquals(expectedConstraints, violationList);
    }

    private static Stream<Arguments> validCreateTaskRequest() {
        return Stream.of(
                Arguments.of(
                        emptyList(), CreateTaskRequestDto.builder()
                                .title("test title")
                                .assigneeId("kaa001")
                                .build()
                )
        );
    }

    private static List<String> validateRequest(CreateTaskRequestDto request) {
        Set<ConstraintViolation<CreateTaskRequestDto>> violations = validator.validate(request);

        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .sorted()
                .toList();
    }
}