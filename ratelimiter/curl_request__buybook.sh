#!/bin/bash

curl --location --request GET 'http://localhost:8910/bookstore3/api/v1/books/available' \
--header 'userid: user1' \