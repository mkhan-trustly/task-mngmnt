### Task management
Task management is the process of overseeing a task through its lifecycle. 
It involves planning, tracking, and reporting. 
Task management can help individuals achieve goals or enable groups of individuals to collaborate and 
share knowledge for the accomplishment of collective goals.

I have tried to keep it very simple, so I am basically working with only few attributes (the simple ones) 
about managing task, like title, description, assignee and few more. More about this can you explore 
in the next section [Purpose](#purpose)

### Purpose

The purpose of this demo project is to show 
- how to build an application from scratch
- try to touch many different things to show what a modern applications would need
- discuss design decisions
- following the design principles.

Why so many things in one application, have I complicated it?
Modern applications requires you to know many things in-order to design a resilient application. 
I have just briefly touched them, the core part is still
- controller
- service
- tasks related classes in domain/model/tasks
- exception handling
- notification part

### **_Focus on the /domain package if you are in a hurry._**

#### Design principles

- RESTful Services
    * Use nouns
      * /tasks/
    * Versioning of API
      * /v1/...
    * Clear request / response
    * Validation of request / response
      * See CreateTaskRequestDto.java
    * Error messages
      * GlobalExceptionHandler.java
- SOLID design
    * Single responsibility principle
      * BasicTask, ChecklistTask
    * Open-closed principle
      * See TaskStrategy.java & factory pattern associated with it.
    * Interface segregation 
      * Task, Auditable, Checklist, Approvable
    * Dependency injection
- Testing
    * Unit test
      * TaskUserTest.java
    * Integration test
      * TaskControllerTest.java
- Domain driver design
    * Bounded context
    * Value objects
      * TaskUser.java
- Structured validations
    * Using jakarta validations
      * See CreateTaskRequestDto.java
- Loosely coupled architecture
    * TaskService & NotificationService are loosely coupled with the help of events.
    * Notification service can be extracted out and run as a micro-service, and instead of Spring events we would require a message broker for communication.
- Logging
  - Enabled JSON logs for better structured logging.
- Microservice design patterns
    * Database per service
    * Distributed tracing

#### With proper infrastructure the following can be achieved.
- High availability
- Resilience
- Scalability

### MongoDB
You need to start mongodb in a docker container in-order to run the application
```
docker run -d -p 27017:27017 --name mongo mongo:latest
```

Some docker commands, in-case you need
```
docker ps -a
docker stop <container-id>
docker start mongo
docker rm -f mongo
```

To start a shell and then 
```
docker exec -it <container_id> mongosh
show collections 
use <database_name>
db.<collection_name>.find()
```

### Tracing
Optional to start, otherwise you can go ahead and start application without it.
```
docker run --rm -p 4317:4317 -p 16686:16686 jaegertracing/all-in-one:latest
```
@WithSpan is added at one place in TaskService#createTask, this would allow us to see 
* span name
* during & timing
* parent-child relationship
```
HTTP GET /tasks   (Parent Span)
  ├── TaskService.createTask  (Child Span)
```

### Running application locally
* Start by running MongoDB in a docker container
* Run TaskManagementApplication.java
* Visit http://localhost:8080/swagger-ui/index.html
  * First authorize yourself, see [Security](#security)

### Naming conventions
- TaskList but not ListTask, domain should be first. It is more clear this way.

### Design decision
#### NoSQL vs SQL
The following factors needs to be considered when choosing the database:
* Data structure (structured or semi-structured)
* Relationships (tasks, users, projects, approvals)
* Consistency (strong ACID compliance, transactions)
* Scalability (vertical or horizontal scaling)
  * High scalability (millions of tasks distributed across users)
* Query flexibility (powerful SQL queries with joins or can be handled in the application layer)

Conclusion: When the task management application grows involves relationships, strong consistency due to the approvals,
reporting relational database can be considered strongly. 

### Security
JWT Token is used for authentication, in the real world application we would 
be using some IdP like Azure AD which validates and issues a JWT.
The JWT is sent to the Spring Boot application (usually as a Bearer token in the Authorization header).
We basically don't validate the JWT Token, it is taken care by the IdP.
In our case we are doing it in the application (see application.yml)

Here is how our payload looks, you can test it on https://jwt.io/
```
{
  "id": "kaa007",
  "name": "John Doe",
  "emailId": "kaa@x.com"
}
```
For simplicity there is no expiry on the token so you can choose any of the following tokens:

```
-- task user: kaa007
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImthYTAwNyIsIm5hbWUiOiJKb2huIERvZSIsImVtYWlsSWQiOiJra2FAZ21haWwuY29tIn0.foqBEDUQAw7Psum7ES4EdFvHwbHoLbqIb_THXPmZaWE

-- task user: aak700
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImFhazcwMCIsIm5hbWUiOiJUaW0gRG9lIiwiZW1haWxJZCI6ImFha2FAZ21haWwuY29tIn0.QEmwb3VF6bA1opYeMazCBDycqUcHG1zcOyNZm-BXRbE
```

Go to swagger and click on right top [Authorize]
http://localhost:8080/swagger-ui/index.html
 
### FAQs
- Todo: A todo is a simple reminder of something that needs to be done. Todos are typically smaller, more immediate, and often personal or routine. 
- Task: A task is a specific unit of work that may involve multiple steps, and is typically part of a larger project or goal. Often requires planning, resources and collaboration.

### Todo
- Update needs to be implemented
- Test with time-zone support
- Move secret to yml