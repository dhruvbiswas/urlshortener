# URLShortener

A URLShortener SpringBoot application

## Available Scripts

In the project directory, you can run:

### `mvn clean package`

Builds the app in the development mode.

### `./build_url_shortener_runtime_container.sh`

Creates Spring Boot runtime container

```REPOSITORY             TAG               IMAGE ID       CREATED         SIZE ```\
```urlshortener.service   latest            0799d971ae1c   5 seconds ago   515MB```

### `Modify mongo db path`

Using a text editor edit the file docker-compose.yml and modify the following lines.\

/Users/databackup/mongodb_data_volume/db:/data/db \
to \
/tmp/databackup/mongo_data_volume/db:/data/db

/Users/databackup/mongodb_data_volume/log:/var/log/mongodb \
to \
/tmp/databackup/mongodb_data_volume/log:/var/log/mongodb

Next create these directories on your local machine

mkdir -p /tmp/databackup/mongodb_data_volume/log \
mkdir -p /tmp/databackup/mongo_data_volume/db

This will ensure that mongodb data is maintained across container restarts

### `./runcompose.sh`

**Note: this would start containers on your machine!**

### `Check what containers get spawned by running docker container ls

Check 2 Spring Boot containers, nginx container and mongodb containers have started

Check the container ids from the above command output and then view its logs by running \
the following

docker logs --follow <container_id>

Open 2 separate terminals and check each Spring Boot container's logs \
Logs should indicate the server started correctly and also should indicate that mongodb \
connection was successfully established

### Shortening a URL

curl -H "Content-Type: application/json" -X POST -d \
'{"longUrl":"https://www.microsoft.com/en-ca/microsoft-teams/download-app#download-for-desktop1"}' \
http://mic.shrt:4000/shorten

This curl request should hit the nginx proxy first and then should hit one of the \
load balanced Spring Boot servers. \
The Server should generate a short URL and send the following response to curl

{"shortURL":"http://mic.shrt:4000/7GOdGY0nq","message":"SUCCESS"}

### Open a browser and visit the short URL 

The browser should open a connection to nginx and then the short url request \
should reach one of the Spring Boot load balanced servers. \
If the long URL for this short URL is not cached in the server then a mongoDB \
request would be sent to fetch the long url. \
The fetched url would be cached in the server and a redirect response would be sent to the \
browser. \
The browser should get re-directed to the actual long url.