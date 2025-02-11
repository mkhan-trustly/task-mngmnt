package se.work.task.management.domain.persistence.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import se.work.task.management.domain.model.task.TaskUser;

@WritingConverter
public class TaskUserWriteConverter implements Converter<TaskUser, String>  {

    @Override
    public String convert(TaskUser value) {
        return value.getUsername();
    }
}
