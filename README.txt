

1) cd to football-api-impl
2) mvn clean install
3) cd to SwaggerParser
4) mvn clean install
5) cd to SwaggerCodegenExtension
6) mvn clean install

7) cd to SwaggerCodegenExtension\modules\swagger-codegen-cli\target

OLD
8) java -jar ./swagger-codegen-cli.jar generate -i ../../../../API/FootballResultsAPI.json -l spring -o ../../../../APIApp

NEW
8) java -jar ./swagger-codegen-cli.jar generate -c ../../../../SwaggerCustomTemplates/swagger-custom/config.json -t ../../../../SwaggerCustomTemplates/swagger-custom/templates/JavaJaxRS -i ../../../../API/FootballResultsAPI.json -l jaxrs -o ../../../../APIApp

9) cd to APIApp
10) mvn clean package jetty:run
11) Visit http://localhost:8080/v1/seasons/1999

Not sure at this point how to get the swagger output to display.
