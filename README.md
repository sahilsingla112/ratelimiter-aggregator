# ratelimiter-aggregator

Provides rate limiting features on edge serve (API gateway). It uses Netflix ZUUL from Sping cloud for API gateway.
Provides an admin API to register a new underlying API and user without restarting the server.
Please refer technical design document for more details on the architecture.

#### It has the following modules:

Module1 -- Configuration service backed by git config file.  
Module2 -- Exposes sample downstream services for test.

Module3 -- API gateway which rate limits the calls to underlying services. 


## Things to install:

1) Redis download (Tested on 3.2.100 version)
   For Windows, https://github.com/microsoftarchive/redis/releases

2) Download gitbash.
https://github.com/git-for-windows/git/releases

## Steps to run and test:

1) Extract ratelimiter-aggregator. Go inside ratelimiter-aggregator and run 'mvn clean install'

2) Open another bash or gitbash terminal, cd configserver/ and execute ./start.sh. By default, it will run on port 8100
Verify it by running http://localhost:8100/ratelimiter-dev.properties

3) Open another bash or gitbash terminal, cd demo-service/ and execute ./start.sh. By default, it will run on port 8915
Verify it by running http://localhost:8915/api/v1/books/available 
You should see 'Spring in Action' 

4) Open another bash or gitbash terminal, cd ratelimiter/ and execute ./start.sh. Be default, it will run on port 8910

After all the modules are up and running in their own terminal:

5) cd ratelimiter/ from the main terminal, execute ./curl_request.sh. 
Monitor the logs on terminal. It should show the total hit count for the current window.

In case, you are not using this script and hitting the test API from some other tool, add header 'userid: user1'
This is how the application will know from which user the request is coming.

6) Execute ./curl_request.sh multiple times, current limit is 3 requests/min. If you do this more than 3 in a minute, following message can be seen on the logs of ratelimiter terminal.
API limit exceeded! Request is rejected for /api/v1/books/available


## How to change the rate limit parameters of existing API:

These values are persisted in testdb/API_INFO and testdb/USER_API_INFO.

http://localhost:8910/h2-console 
User: admin
Password: password


1)	PUT http://localhost:8910/admin/api
{
	"id": 1,
	"defaultLimitPerMinute":5,
	"rateLimitStrategy": "BETTER_FIXED_WINDOW"
}

	
2)	PU http://localhost:8910/admin/user
{
  "apiId":1,
   "username":"user1",
   "rateLimitPerMinute":5
}
	
So, now the user1 and API of id =1 will have ratelimit updated to 5 requests/minute. 
You can verify this without restart by executing step6).

## Setup Configuration server:

This is not needed if you are not going to change Zuul configuration to add any new route (new underlying API). But if you wish to test that feature, you will need to perform the following steps:

1)	To add any new API, you need to add a new route in Zuul configuration. All this is in a Configuration file which is stored in a Git repository.

Log into your Github account. Create a new repository config-repo. Copy the file ratelimiter-dev.properties from https://github.com/sahilsingla112/config-repo/ to this new repo. 
		
2)	Once you upload the updated file on your new git repository. Change the following configuration in src/main/application.properties of configserver module.

spring.cloud.config.server.git.uri=https://github.com/<youraccountname>/config-repo
spring.cloud.config.server.git.username=<yourusername>
spring.cloud.config.server.git.password=<yourpassword>


## How to add a new API to the system without restart:

1)	To add a new API, modify the ratelimiter-dev.properties and commit the changes to config-repo.
	
	zuul.routes.abcname.path: /abcname/**
	zuul.routes.abcname.url: http://<hostname>:<port>

	hostname and port are from the new API.
	abcname is any name you can choose for this new API.  
	
2)	Verify the changes from http://localhost:8100/ratelimiter-dev.properties


3)	POST http://localhost:8910/admin/api
{
	"name":"bookstore4",

	"downStreamServiceUrl": "http://localhost:8915",

	"downStreamApiUri": "/api/v1/books/buy",

	"defaultLimitPerMinute": 5,
	"refresh":true,
	"rateLimitStrategy":"TUNABLE_SLIDING_WINDOW",
	"accuracyLevel":3
}

Expected response:
{
    "message": "Registration is successul",
    "apiId": 4
}



4)	POST http://localhost:8910/bookstore4/api/v1/books/buy
{
	"name": "Inferno"
}

**** Please add header 'userid: user1' in your POST request. ****

If you repeat this more than the configured rate limit above. You will get this.
	
API limit exceeded! Request is rejected for /api/v1/books/buy

Alternatively, you could register a user also to give user+api specific rate limit.
