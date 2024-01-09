#!/bin/bash

docker container prune -f

docker rmi -f urlshortener.service:latest
docker rmi -f urlshortener.build:latest
docker rmi -f mongo:latest
docker rmi -f nginx:latest

rm -rf target
