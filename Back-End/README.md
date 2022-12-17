# Restaurant-Web-Project Backend Server

This backend server is based on Java Spring Rest Api.


## Functionality

-  Authentication with roles (user,staff,owner) returning authentication Token.
-  Control of items,users,deliveries,restaurant information and reservations depend of the user role.
-  Reset password when you forgot your password with email token verification.
-  Get menu items.
-  Get daily lunchmenu items.
-  Get active,delivered,history of deliveries.
-  Get active reservations,history of reservations.
-  Sheduletask for cleaning unnecessary tokens.

## Core Information

 - Rest token is sending via smtp-server, used JavaMailSender from spring-boot-starter-mail dependency.
 - Using Spring Security with JwtToken filter for returning Authentication token after login or register, using io.jsonwebtoken dependancy.
 - Using ModelMapper for mapping between DTO and Entities
 - Using MySQL database
 - Validation DTO with spring validator, spring-boot-starter-validation dependancy.
 - Using BCryptPasswordEncoder for encoding passwords


## Paths
### Authentication
- POST `/auth/login, Body`: Authenticate in backend server. <details>**Returns:** `JWT Token` <br />**Header name:** `Authorization` <br /> **Body:** `username, password`</details>
- POST `/auth/register, Body` Register new user. <details>**Returns:** `JWT Token` <br />**Header name:** `Authorization` <br /> **Body:** `username, password` <br /> **Validations**: <ul><li>`Email must be valid!`</li><li>`Password must be at least 5 characters!`</li></ul></details>
- POST `/auth/reset-password/verification/send , Object.of(String email, String url)` Url must be the link of path that will be generated in email etc if your path url is "127.0.0.1/reset-password/verify-token", client will receive email with url for verification -> "http://127.0.0.1/reset-password/verify?token=%s&email=%s".<details style="display: inline">"wqrq</details>
- PUT `/auth/reset-password/{email} , Object.of(String newPassword, String token)` Password must be at least 5 symbols! <details style="display: inline">"wqrq</details>
### Restaurant core (OWNER role)

- GET `/core/get-data` return json with restaurant core information
- PUT `/core/edit-data , Object.of(restaurant core information)`

