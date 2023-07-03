#!/bin/bash

#create
curl -i -X POST \
--url http://localhost:8001/services/ \
--data 'name=user-service' \
--data 'url=http://localhost:8080/q/swagger-ui/subscriptionService'


curl -i -X POST \
--url http://localhost:8001/services/ \
--data 'name=carManufacturer-service' \
--data 'url=http://localhost:8080/q/swagger-ui/carManufacturerService'


curl -i -X POST \
--url http://localhost:8001/services/ \
--data 'name=apilot-service' \
--data 'url=http://localhost:8080/q/swagger-ui/apilotService'

#create routes
curl -i -X POST \
--url http://localhost:8001/services/carManufacturer-service/routes \
--data 'hosts[]=carManufacturer.com'

curl -i -X POST \
--url http://localhost:8001/services/user-service/routes \
--data 'hosts[]=user.com'

curl -i -X POST \
--url http://localhost:8001/services/apilot-service/routes \
--data 'hosts[]=apilot.com'

#invoke the services using KONG
curl -i -X POST -H "Content-Type: json;charset=UTF-8" \
--url "http://${KONG_SERVER_ADDRESS}:8000/" \
--header "Host: serverlambda.com" \
--data "@body.json"

curl -i -X GET \
--url "http://${KONG_SERVER_ADDRESS}:8000/" \
--header "Host: serveruser.com" 

curl -i -X GET \
--url "http://${KONG_SERVER_ADDRESS}:8000/" \
--header "Host: serverapilot.com" 

curl -i -X GET \
--url "http://${KONG_SERVER_ADDRESS}:8000/" \
--header "Host: servercarmanufacturer.com" 