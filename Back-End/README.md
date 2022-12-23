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
- POST `/auth/login , Body`: Authenticate in backend server. <details>**Returns:** `Token` <br />**Header name:** `Authorization` <br /> **Body:** `username, password` <br /> **Status codes**: `200,403`</details>
- POST `/auth/register , Body` Register new user. <details>**Returns:** `Token` <br />**Header name:** `Authorization` <br /> **Body**: `username, password`<br /> **Status codes**: `200,400(with body error)` <br /> **Validations**: <ul><li>`Email must be valid!`</li><li>`Password must be at least 5 characters!`</li></ul></details>
- POST `/auth/reset-password/verification/send , Body` Sending verification email <details>**Returns**: `Status`<br /> **Status codes**: `200,400(with body error)` <br />**Body**:<ul><li>`email`</li><li>`url`: **Url must be the link of path that will be generated in email etc if your path url is "127.0.0.1/reset-password/verify-token", client will receive email with url for verification -> "http://127.0.0.1/reset-password/verify?token=%s&email=%s".** </li></ul></details>
- PUT `/auth/reset-password/{email} , Body)` Verify token and reset password. <details>**Returns**: `Status`<br />**Status codes**: `200,400(with body error)` <br />**Body**: email,token <br /> **Validations**: <ul><li>`Password must be at least 5 characters!`</li></ul></details>
### Restaurant core (req OWNER role)

- GET `/core`: Restaurant core information
- PUT `/core , Body` <details>**Returns**: `Status`<br />**Status Codes:** `200,400`<br />**Body:** `name,description,openTime,closeTime,guestsStatus,openDay,closeDay,firmName,deliveryStatus,ownerNames,contactsPhoneNumber,location`<br />**Validations:**<ul><li>`Restaurant name must be between 3 and 20 chars!`</li><li>`Restaurant description must be max 50 chars!`</li></ul></details>

### User (Authentication req)

- GET `/user`: Return user data of authenticated user.
- GET `/user/{email}`: (Req OWNER role): Return user data of user with email.
- GET `/roles`: Return active roles.
- PUT `/user , body`: Edit authenticated user. <details>**Returns:** `Status`<br />**Status Codes:** `200,400(with error body)`<br />**Body:** `email,oldPasword,newPassword,address[]`<br />**Validations:** <ul><li>`Password must be at least 5 symbols!`</li><li>`Email must be valid!`</li></ul><br />**Information**: <ul><li>`Single address body is: id,name,city,village,street,number,entrance,floor,apartmentNumber`</li><li>`Address body can be without id, if it is with id server will search for this address, else server will create new record.`</li><li>`oldPassword, can be null`</li></ul></details>
- GET `/user/control/{email}` (Req OWNER role): Return settings for controlling user.
- PUT `/user/control/{email}` (Req OWNER role): Edit control settings of user. <details>**Returns:** `Status`<br />**Status codes:** `200,400`<br />**Body:** `enabled,roles`<br />**Information:** `Single role body accept only name.`</details>

### Categories