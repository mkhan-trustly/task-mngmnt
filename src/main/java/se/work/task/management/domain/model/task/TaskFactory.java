package se.work.task.management.domain.model.task;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class TaskFactory {

    private final Map<TaskType, Supplier<TaskStrategy>> taskStrategies = new HashMap<>();

    public TaskFactory() {
        taskStrategies.put(TaskType.BASIC, BasicTaskStrategy::new);
    }

    public TaskStrategy getTaskStrategy(TaskType taskType) {
        var taskSupplier = taskStrategies.get(taskType);
        if (taskSupplier == null) {
            throw new UnsupportedOperationException("Unknown task type: " + taskType);
        }
        return taskSupplier.get();
    }
}
