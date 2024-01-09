URLShortener is a Spring Boot based URL Shortening service
The service runs a Spring Boot Application and exposes the following endpoints

- /shorten -> accepts a post request containing a long url and generates a short url
  
  Sample curl request
  curl -H "Content-Type: application/json" -X POST -d '{"longUrl":"https://www.microsoft.com/en-ca/microsoft-teams/download-app#download-for-desktop1"}' http://mic.shrt:4000/shorten

  Sample curl response
  {"shortURL":"http://mic.shrt:4000/7GOdGY0nq","message":"SUCCESS"}
  
- Once you get a short URL and if you open the short URL in a browser, the service would re-direct the user to the actual long url

- The service creates short urls with hostname as mic.shrt. Add an entry in /etc/hosts as
  127.0.0.1 mic.shrt
  
- How to build and execute the sources

  Checkout the sources using git clone -b develop https://github.com/dhruvbiswas/urlshortener.git

  Build the sources by running
  
  mvn clean package

  Build Spring Boot runtime container by running
  
  ./build_url_shortener_runtime_container.sh

  Change the mongodb volume mounts in the file docker-compose.yml to a path that is available on your machine
  Change lines in the file that begin with /Users/databackup/ and change it to, lets say, /tmp/mongo/
  Keep the rest of the path the same.
  
  So you should have lines similar to (make sure /tmp/mongo/mongodb_data_volume/db and /tmp/mongo/mongodb_data_volume/log path exists)
  
  /tmp/mongo/mongodb_data_volume/db:/data/db
  /tmp/mongo/mongodb_data_volume/log:/var/log/mongodb
  
  Start set of services by running
  ./runcompose.sh

  Wait for all containers to start
  docker container ls

  CONTAINER ID   IMAGE                         COMMAND                  CREATED          STATUS          PORTS                                               NAMES
126ff0e4f43c   nginx:latest                  "/docker-entrypoint.…"   7 seconds ago    Up 2 seconds    80/tcp, 0.0.0.0:4000->4000/tcp, :::4000->4000/tcp   nginx
25927e529a20   urlshortener.service:latest   "java -jar /urlshort…"   11 seconds ago   Up 6 seconds    8080/tcp                                            urlshortener_urlshortener_2
e5c0199b840c   urlshortener.service:latest   "java -jar /urlshort…"   11 seconds ago   Up 6 seconds    8080/tcp                                            urlshortener_urlshortener_1
f93e7ac54241   mongo:latest                  "docker-entrypoint.s…"   14 seconds ago   Up 10 seconds   0.0.0.0:27017->27017/tcp, :::27017->27017/tcp       mongodb

  Open 2 separate terminals and Check that Spring Boot containers started successfully
  
  docker logs --follow 25927e529a20 -> this is the container_id of urlshortener.service:latest
  docker logs --follow e5c0199b840c -> this is the container_id of urlshortener.service:latest

  Docker logs should show that Spring Boot started and each container connected to MongoDB

  Send curl requests as shown above to create short urls and to visit short urls created by the service.
