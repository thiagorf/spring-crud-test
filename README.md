# spring-crud-test
> Just trying spring boot

Just to test spring boot core functionalities (beans, annotations, micro services, tests)

## How to run?

> Requirements: jdk-11

* Rename the file "application-dev.properties" to "application.properties" in src/main/resource and give the respective required configuration (database name, port number, etc). 
* Run the start command (intellij or eclipse)

> With docker

coming soon...

## Implementations

- [x] CRUD resources
- [x] Authentication/authorization with jwt (Custom UserDetails, UserDetailsService, Basic jwt filter)
- [ ] Cookie token instead of Bearer header
- [ ] Lombok annotations
- [ ] Advanced jpa relationships
- [ ] Tests
- [x] Swagger docs (Meta programming is really convenient)
- [ ] Mini microservices
- [x] Docker container with database configuration

## Api Documentation
1. Run spring boot
    * intellij or eclipse start command
2. check swagger-ui.html (http://localhost:8080/swagger-ui.html)
