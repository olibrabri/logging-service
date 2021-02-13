# logging-service
Repository for logging service

With this service you can print all the server logs which are saved in temp folder.
Also you can create a server log, start it, complete job and terminate it.

To retrieve or get all logs
curl -X GET \
  http://localhost:8080/server/logs/retrieve/all \
  -H 'cache-control: no-cache' \
  -H 'postman-token: d227bc54-c046-2a07-3268-312a5a89bf21'
  
To retrieve a specific server log
curl -X GET \
  http://localhost:8080/server/logs/retrieve/Olimpo/log \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 9e29eb90-004d-5054-3466-054485063345'
  
To start your own server log
curl -X POST \
  http://localhost:8080/server/logs/start \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 1943b17f-a675-4e10-f472-7adb9c8a6648' \
  -d '{
	"serverName":"Apollo"
}'

To start a random server
curl -X POST \
  http://localhost:8080/server/logs/start \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 71009440-1da4-e68f-88db-a0dafd55ef60' \
  -d '{
	"serverName":""
}'

To complete a job for a specific server log
curl -X POST \
  http://localhost:8080/server/logs/complete \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: a186a4a8-1a84-6475-ed19-6bc71889ef4a' \
  -d '{
	"serverName":"bue8Q3Vy"
}'

To terminate a server log
curl -X POST \
  http://localhost:8080/server/logs/terminate \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 672fb36e-be17-e028-8f79-21b02ecb0634' \
  -d '{
	"serverName":"Columbia"
}'
