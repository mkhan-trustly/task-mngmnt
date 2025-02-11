package se.work.task.management.domain.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import se.work.task.management.domain.model.task.Task;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
}
