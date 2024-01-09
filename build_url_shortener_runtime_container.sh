#!/bin/bash

echo "Creating runtime container"
docker build -t urlshortener.service -f Dockerfile .
