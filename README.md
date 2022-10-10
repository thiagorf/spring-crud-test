# spring-crud-test
> Just trying spring boot

Just to test spring boot core functionalities (beans, annotations, micro services, tests)

## Points of Interest

Custom [UserDetails](https://github.com/thiagorf/spring-crud-test/blob/master/src/main/java/com/api/crudapi/security/auth/AuthUserDetails.java) and [UserDetailsService](https://github.com/thiagorf/spring-crud-test/blob/master/src/main/java/com/api/crudapi/security/auth/AuthUserDetailsService.java)

[Security Config without WebMvcConfigurerAdapter](https://github.com/thiagorf/spring-crud-test/blob/master/src/main/java/com/api/crudapi/security/SecurityConfig.java) Prefer @Bean to @Autowired (it's easier to test later)

Docker compose for [containerized development](https://github.com/thiagorf/spring-crud-test/blob/master/docker-compose.yml) and [Multistage build Dockerfile](https://github.com/thiagorf/spring-crud-test/blob/master/Dockerfile) 

[Security Filter Unit test](https://github.com/thiagorf/spring-crud-test/blob/master/src/test/java/com/api/crudapi/security/JwtTokenFilterTest.java) using the ThreadLocal to check SecurityContextHolder

[Integration Test on Protected endpoint](https://github.com/thiagorf/spring-crud-test/blob/master/src/test/java/com/api/crudapi/vehicle/VehicleControllerTest.java) the FilterChainProxy is mocked, everything you define on ContextConfiguration will have to be mocked (VehicleController and SecurityConfig dependencies) 

[Object to String test Util](https://github.com/thiagorf/spring-crud-test/blob/master/src/test/java/com/api/crudapi/util/JsonUtil.java)

[Custom Exceptions](https://github.com/thiagorf/spring-crud-test/tree/master/src/main/java/com/api/crudapi/exceptions)

## How to run?

> Requirements: jdk-11

* Rename the file "application-dev.properties" to "application.properties" in src/main/resource and give the respective required configuration (database name, port number, cookie name, secret, etc). 
* Run the start command (intellij or eclipse)

> With docker

* Run docker compose up --build (docker-compose in older versions) to create the container for the first time (Dockerfile image)
* Ctrl + c to kill the running container
* to run the container again, docker compose up

## Implementations

- [x] CRUD resources
- [x] Authentication/authorization with jwt (Custom UserDetails, UserDetailsService, Basic jwt filter)
- [x] Cookie token instead of Bearer header
- [x] Lombok annotations
- [ ] Advanced jpa relationships (same as microservices point)
- [x] Tests
- [x] Swagger docs (Meta programming is really convenient)
- [ ] Mini microservices (It's going to be another project, check my github profile for it)
- [x] Docker container with database configuration

## Api Documentation
1. Run spring boot
    * intellij or eclipse start command
2. check swagger-ui.html (http://localhost:8080/swagger-ui.html)
