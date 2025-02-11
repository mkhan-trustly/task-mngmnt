package se.work.task.management.domain.model.task;

import lombok.Getter;
import se.work.task.management.application.exception.UnsupportedJwtTokenException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
public class TaskUser {

    private final String username;

    public TaskUser(Map<String, Object> claims) {
        if (claims == null) {
            throw new UnsupportedJwtTokenException("JwtToken not recognized, claims are missing");
        }

        if (!claims.keySet().containsAll(List.of("id", "name", "emailId"))) {
            throw new UnsupportedJwtTokenException("JwtToken not recognized, missing required details");
        }

        this.username = validateUsername(claims.get("id").toString());
    }

    public TaskUser(String username) {
        this.username = validateUsername(username);
    }

    private String validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }

        if (!username.matches("^[a-zA-Z]{3}\\d{3}")) {
            throw new IllegalArgumentException("Username is not valid");
        }
        return username.strip().toLowerCase();
    }

    public String toString() {
        return this.username;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;

        if (that == null || getClass() != that.getClass()) return false;
        TaskUser user = (TaskUser) that;
        return this.username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }
}