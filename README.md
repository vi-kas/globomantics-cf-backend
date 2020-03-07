# globomantics-cf-backend

Contains codebase for different modules from Pluralsight course - **Scala Specific Design Patterns**

The course is divided in modules, hence for each, code is kept in directory called *module_x*. E.g. *module_3*

To run **project** specific to a particular module:
- Go to respective directory `cd modules/module_3`
- Give `sbt run` to run the service

Above command should run the service at `localhost:8080`.
You may use any REST Client e.g. [POSTMAN](https://www.getpostman.com) to test the service endpoints.

E.g. To create a new User, you may send a **POST** request to *http://localhost:8080/api/v1/users* with below request JSON (Content-type: application/json).

    {
    	"id": "323c78ec-1eb8-4410-b284-f75079b15708",
    	"name": "Alex Bar",
    	"email": "Alex_bar@mail.com",
    	"password": "plain_pwd",
    	"address": {
    		"addressLine": "04-444, Block 444",
    		"location": {
    			"pin": "302012",
    			"city": "Jaipur",
    			"country": "India"
    		}
    	},
    	"role": "Speaker"
    }

Above request should give you a success response, if everything is ok.

**Note:**
The project (module_4 onwards) uses *Docker* for running *postgres* database. So to follow along, ensure **Docker** is installed on your machine and running.

Once **Docker** is running:
 - Create a directory for docker volume
    e.g. On Mac/Linux: `mkdir -p $HOME/docker/volumes/postgres`
  - Run the postgres image
    `docker run --rm --name pg-docker -e POSTGRES_PASSWORD=postgres -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data postgres`
  - Ensure if it's running:
 `docker ps` The command should show something like:
 `7bc71fbb33e0        postgres            "docker-entrypoint.s…"   14 minutes ago      Up 14 minutes       0.0.0.0:5432->5432/tcp   pg-docker`

Configure *postgres* by following below steps:
- Enter inside interactive *bash* shell on the container to use postgres
`docker exec -it pg-docker /bin/bash`
- Open *psql* for *localhost*
`psql -h localhost -U postgres -d postgres`
Above command says *Open psql server running on localhost under user postgres and connect to database named postgres*
- Create a **database**, a **user**, and provide permissions on database to the user created.
```
CREATE DATABASE "pgconfedb" WITH ENCODING 'UTF8';
CREATE USER "trustworthy" WITH PASSWORD 'trust';
GRANT ALL ON DATABASE "pgconfedb" TO trustworthy;
```
This should set *postgres* with desired configuration. Above configuration(i.e. databaseName, user and password) is required in project's `resources/application.conf` file:
```
conferencedb = {
  connectionPool = "HikariCP"
  dataSourceClass = "org.postgresql.ds.PGSimpleDataSource"
  properties = {
	  serverName = "localhost"
	  portNumber = "5432"
	  databaseName = "pgconfedb"
	  user = "trustworthy"
	  password = "trust"
	  url = "jdbc:postgresql://localhost:5432/pgconfedb"
  }
  numThreads = 10
}
```

**Note:**
Module 6 uses API Key to access google maps API, you may [change](https://developers.google.com/maps/documentation/javascript/get-api-key) the [configuration](modules/module_6/src/main/resources/application.conf).  For obvious reasons, this key will no longer work.
```
//This key will no longer work
maps {
  apiKey = "OIUYyBLPJHT-e4Mze9li216YDsmedpKrdIlIiU"
}
```
