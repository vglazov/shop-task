### Running database
Please use docker to run mysql database

`docker run --rm -p 3306:3306 -v {project directory}/initdb.d:/docker-entrypoint-initdb.d:ro mysql/mysql-server:latest`

### Running api server
`./mvnw spring-boot:run`

Api is exposed to 8080 port.

This is a draft work, only basic methods are implemented. No validations or proper error handling. No logging.

You may find api call samples in generated [documentation](./docs/index.html)
