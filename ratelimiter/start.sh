#!/bin/bash

java -Dspring.cloud.config.uri=http://localhost:8100 -jar target/ratelimiter*.jar
