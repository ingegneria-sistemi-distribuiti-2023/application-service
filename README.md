# application-service

Database [here](https://github.com/ingegneria-sistemi-distribuiti-2023/application-db)

Build the application using Maven

```bash
mvn clean install
```

Start the application

```bash
mvn spring-boot:run
```

Is also possible build a package of the application in jar file:

```bash
mvn package
```

Deploy application on a server:

```bash
java -jar target/application-service-0.0.1-SNAPSHOT.jar
```

## Tip

Be sure are you using java 17 or switch to right version using SDKMAN: 

```bash
sdk use java 17.0.5-tem
```
