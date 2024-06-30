# Green-Team-Public-Safety-API
## Table of Contents
1. [Authentication](#authentication)
2. [User Account Management](#user-account-management)
3. [Message](#message)

### Authentication
#### Login
- Endpoint: `/api/users/authenticate`
- HTTP Method: `POST`
- Description: Authentication is done using spring security and a jwt token is issued for successful authentication
- Request Parameters: `applications/json`
    * `name` (string, required): user's full name
    * `email` (string, required): user's valid email
    * `password` (string, required): user's password
    ```json
      {
        "name": "John Doe",
        "email": "johndoe@email.com",
        "password": "3sc8&*3@#idGiuD"
      }
    ```

##### Response:
###### Success 
- `Ok - HTTP 200`
  ```json
    {
      "token": "jwt-access-token"
    }
  ```
###### Failure 
- Unauthorized - HTTP 401
  ```json
    {
      "error": "invalid credentials"
    }
  ```
  
### User Account Management
#### Register User
- Endpoint:  `/api/users/register`
- HTTP Method: `POST`
- Description: Accepts an application/json request with request parameters and creates a new user with the request details in database
- Request Parameters: `applications/json`
  * `name` (string, required): user's full name
  * `email` (string, required): user's valid email
  * `password` (string, required): user's password
    ```json
      {
        "name": "John Doe",
        "email": "johndoe@mail.com",
        "password": "3sc8&*3@#idGiuD"
      }
    ```

##### Response
###### Success 
- created - HTTP 201
  ```json
    {
      "id": "1",
      "name": "John Doe",
      "email": "johndoe@mail.com"
    }
  ```
###### Failures 
- Bad Request - HTTP 400 : missing request parameters
  ```json
    {
      "errors":
        {
          "name": "name is required",
          "email": "email is required",
          "password": "password is required"
        }
    }
  ```
- Bad Request - Http 400: user already exist with email
    ```json
      {
       "error": "user with email already exists"
      }
    ```
  
### Message
#### Send Message
- Endpoint:  `/api/messages/new`
- HTTP Method: `POST`
- Description: Accepts an application/json request with request parameters and send a new message
- Request Parameters: `applications/json`
  * `message` : String, required (message the user want to send)
  * `receiverId`: Long, required (userId of the receiver)
  * `senderId`: Long, required (userId of the sender)
  ```json
    {
      "message": "Hey watxup",
      "receiverId": 398,
    }
  ```
##### Response
###### Success
- Ok - Http 200
  ```json
     {
        "id": 1
        "message": "Hey watxup",
        "senderId": "1"
     }
  ```
###### Failure
- NotFound - Http 404
 ```json
    {
      "error": "reciever not found with id 398"
    }
 ```
- BadRequest - Http 400
 ```json
    {
      "message": "message is required",
      "recieverId": "recieverId is required",
    }
 ```
#### Get messages
- Endpoint - `/api/messages/:userId`
- HttpMethod - `GET`
- Description: retrieves all messages between the authenticated user and the senderId
##### Response
##### Success
- Ok - Http 200
 ```json
  {
    "sent": [
        {
          "id": "2",
          "message": "waxup",
          "timeStamp": "2024-06-30T09:48:40.123+00:00",
        }, {
          "id": "3",
          "message": "waxup",
          "timeStamp": "2024-06-30T09:48:40.123+00:00",
        }, {
          "id": "4",
          "message": "waxup",
          "timeStamp": "2024-06-30T09:48:40.123+00:00",
        },
      ],
    "received": [
      {
        "id": 8,
        "messsage": "taddup",
        "timestamp": "2024-06-30T09:48:40.123+00:00"
      }, {
        "id": 19,
        "messsage": "taddup",
        "timestamp": "2024-06-30T09:48:40.123+00:00" 
      } {
        "id": 28,
        "messsage": "taddup",
        "timestamp": "2024-06-30T09:48:40.123+00:00"
      }
    ]
  }
```
