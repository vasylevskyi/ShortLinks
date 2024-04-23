### ShortLinks
#### Java GraalVM 17.0.9

### Profile dev
#### В application.properties added Н2 console, http://localhost:8080/h2-console ; user sa
#### User/pass john.doe created as hash
#### to run: java -jar ShortLinks-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

### Profile prod
#### Need to add Environment
#### PostgresSQL 16
#### Environment variables:
#### DB_USERNAME=postgres
#### DB_PASSWORD=postgres
#### to run: java -jar ShortLinks-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod

### Docker
#### docker-compose --profile prod up -d