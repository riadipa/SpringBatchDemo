# spring-batch
A demo application for spring-batch using DSL. 
The spring batch is configured to save job execution data in its own relational database (not in-memory) 

#### start the databases from docker file:
```
spring-batch\docker>docker-compose -f docker-compose-localdb.yml up -d
```

#### shutdown and remove the databases

```
spring-batch\docker>docker-compose -f docker-compose-localdb.yml down --volume
```

