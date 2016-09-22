

1) cd to football-api-impl
2) mvn clean install
3) cd to SwaggerParser
4) mvn clean install
5) cd to SwaggerCodegenExtension
6) mvn clean install
7) cd to SwaggerCodegenExtension\modules\swagger-codegen-cli\target
8) java -jar ./swagger-codegen-cli.jar generate -i ../../../../API/FootballResultsAPI.json -l kf-spring -o ../../../../APIApp
9) cd to APIApp
10) mvn clean package spring-boot:run
11) Visit http://localhost:8080/v1

