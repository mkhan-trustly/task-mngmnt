package se.work.task.management.application.configuration;

import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import se.work.task.management.domain.persistence.converter.TaskUserReadConverter;
import se.work.task.management.domain.persistence.converter.TaskUserWriteConverter;

import java.util.List;

@Configuration
@EnableMongoAuditing
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "tasks";
    }

    @Override
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(List.of(new TaskUserReadConverter(), new TaskUserWriteConverter()));
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(MongoClients.create(), "tasks");
    }
}