# Green-Team-Public-Safety-API
## Table of Contents
1. [Authentication](#authentication)
2. [User Account Management](#user-account-management)

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
      "message": "invalid credentials"
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
      "errors": [
        {
          "name": "name is required",
          "email": "email is required",
          "password": "password is required"
        }
      ] 
    }
  ```
- Bad Request - Http 400: user already exist with email
    ```json
      {
       "message": "user with email already exists"
      }
    ```