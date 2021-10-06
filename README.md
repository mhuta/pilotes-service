## Pilotes Order Service

“Pilotes” are a Majorcan recipe that consisting of a meatball stew.


### Run description

To build the project issue:
```shell
mvn clean install
```

To run Spotbugs tool issue:
```shell
mvn spotbugs:check
```

To build docker image with application issue:
```shell
mvn spring-boot:build-image
```

To run docker image issue:
```shell
docker run -p 9090:8080 -t docker.io/library/backend-technical-test:2.0.0-SNAPSHOT
```

## Author
Mateusz Huta