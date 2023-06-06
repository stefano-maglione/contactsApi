
# ContactsApi

This API exposes endpoints to manage persons and skills.

## Documentation

Once started, you can navigate to http://localhost:8080/swagger-ui/index.html to view the Swagger Resource Listing.
            
The endpoints require Basic Auth to be accessed, preloaded users can be used:



    | User   | Password |
    | - -    | - - - -  |
    | mickey | cheese   |
    | - - - -| - - - -  |
    | admin  | admin    |









## Usage/Examples
Create a skill

```
curl --location 'http://localhost:8080/api/v1/skills' \
--header 'Content-Type: application/json' \
--header 'Accept: application/json' \
--header 'Authorization: Basic bWlja2V5OmNoZWVzZQ==' \
--data '{
  "name": "java",
  "level": "senior"
}'
```


Create a person
```
curl --location 'http://localhost:8080/api/v1/persons' \
--header 'Content-Type: application/json' \
--header 'Accept: application/json' \
--header 'Authorization: Basic bWlja2V5OmNoZWVzZQ==' \
--data-raw '{
  
  "firstname": "Mark",
  "lastname": "Rain",
  "email": "email@gmail.com",
  "mobilePhone": "0762016235",
  "city": "zurich"

}'
```

Add a skill to a person
```
curl --location --request PUT 'http://localhost:8080/api/v1/persons/1/skills/1' \
--header 'Accept: application/json' \
--header 'Authorization: Basic bWlja2V5OmNoZWVzZQ==' 


```
Retrieve all skills owned by a person
```
curl --location 'http://localhost:8080/api/v1/persons/1/skills' \
--header 'Accept: application/json' \
--header 'Authorization: Basic bWlja2V5OmNoZWVzZQ==' 
```

Retrieve all persons that has a specific skill
```
curl --location 'http://localhost:8080/api/v1/skills/1/persons' \
--header 'Authorization: Basic bWlja2V5OmNoZWVzZQ==' 

```



## Tech Stack

SpringBoot 3, Java 17, Spring Security 6

