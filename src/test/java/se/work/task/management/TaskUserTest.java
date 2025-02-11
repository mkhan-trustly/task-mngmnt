package se.work.task.management;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import se.work.task.management.application.exception.UnsupportedJwtTokenException;
import se.work.task.management.domain.model.task.TaskUser;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TaskUserTest {

    @Nested
    class Claims {

        @ParameterizedTest
        @MethodSource("invalidClaims")
        void shouldThrowExceptionOnInvalidClaims(Map<String, Object> claims) {
            assertThrowsExactly(UnsupportedJwtTokenException.class, () -> new TaskUser(claims));
        }

        private static Stream<Arguments> invalidClaims() {
            Map<String, Object> claims = null;
            return Stream.of(
                    Arguments.of(claims),
                    Arguments.of(Map.of()),
                    Arguments.of(Map.of("unknown", "unknown")),
                    Arguments.of(Map.of("id", "1", "name", "kong", "email", "x@x.com"))
            );
        }

        @ParameterizedTest
        @MethodSource("validClaims")
        void shouldCreateTaskUserOnValidClaims(String expectedUsername, Map<String, Object> claims) {
            Assertions.assertEquals(expectedUsername, new TaskUser(claims).getUsername());
        }

        private static Stream<Arguments> validClaims() {
            return Stream.of(
                    Arguments.of("kaa001", Map.of("id", "kaa001", "name", "kong", "emailId", "x@x.com"))
            );
        }
    }

    @Nested
    class Username {

        @ParameterizedTest
        @MethodSource("invalidUsername")
        void shouldThrowExceptionOnInvalidUsername(String username) {
            assertThrowsExactly(IllegalArgumentException.class, () -> new TaskUser(username));
        }

        private static Stream<Arguments> invalidUsername() {
            String username = null;
            return Stream.of(
                    Arguments.of(username),
                    Arguments.of(" "),
                    Arguments.of("öäåö"),
                    Arguments.of("12345"),
                    Arguments.of("abc-122"),
                    Arguments.of("abcd012"),
                    Arguments.of("abc0122")
            );
        }

        @ParameterizedTest
        @MethodSource("validUsername")
        void shouldCreateTaskUserOnValidUsername(String expectedUsername, String username) {
            Assertions.assertEquals(expectedUsername, new TaskUser(username).getUsername());
        }

        private static Stream<Arguments> validUsername() {
            return Stream.of(
                    Arguments.of("kaa001", "kaa001"),
                    Arguments.of("zoa999", "zoa999")
            );
        }
    }





}