package se.work.task.management.domain.persistence.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import se.work.task.management.domain.model.task.TaskUser;

@ReadingConverter
public class TaskUserReadConverter implements Converter<String, TaskUser> {

    @Override
    public TaskUser convert(String value) {
        return new TaskUser(value);
    }
}
