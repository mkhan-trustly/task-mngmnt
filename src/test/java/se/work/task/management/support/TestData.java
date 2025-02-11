package se.work.task.management.support;

import org.springframework.security.oauth2.jwt.Jwt;
import se.work.task.management.domain.model.task.AbstractTask;
import se.work.task.management.domain.model.task.Task;
import se.work.task.management.domain.model.task.TaskUser;
import se.work.task.management.domain.persistence.BasicTask;

import java.lang.reflect.Field;
import java.util.UUID;

public class TestData {

    public static Jwt getTestJwtClaims(String user) {
        return Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("id", user)
                .claim("name", "test")
                .claim("emailId", "x@x.com")
                .build();
    }

    public static Task buildBasicTask(String id) {
        var user = new TaskUser("tst001");
        var task = new BasicTask("test title", null, null, null, user, user);
        setId(task, id);
        return task;
    }

    private static void setId(BasicTask task, String id) {
        try {
            Field field = AbstractTask.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(task, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
