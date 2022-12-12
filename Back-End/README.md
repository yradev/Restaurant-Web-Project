# Restaurant-Web-Project Backend Server

This backend server is based on Java Spring Rest Api.


## Functionality

-  Authentication with roles (user,staff,owner) returning authentication Tooken.
-  Control of items,users,deliveries,restaurant information and reservations depend of the user role.
-  Reset password when you forgot your password with token.
-  Get menu items.
-  Get daily lunchmenu items.
-  Get active,delivered,history of deliveries.
-  Get active reservations,history of reservations.
-  Sheduletask for cleaning unnecessary tookens.

## Core Information

 - Rest tooken is sending via smtp-server used JavaMailSender from spring-boot-starter-mail dependency.
 - Using Spring Security with JwtTooken filter for returning Authentication tooken after login or register, using io.jsonwebtoken dependancy.
 - Using ModelMapper for mapping between DTO and Entities
 - Using MySQL database
 - Validation DTO with spring validator, spring-boot-starter-validation dependancy.
 - Using BCryptPasswordEncoder for encoding passwords


## Paths
### Authentication
- POST `/login , Object.of(String email, String password)` Returns auth token (Tooken must be in header with name "Authorization").
- POST `/register , Object.of(String email, String password)` Returns auth token (Tooken must be in header with name "Authorization"). Email must be valid! Password must be at least 5 symbols!
- POST `/reset-password/verification/send , Object.of(String email, String url)` Url must be the link of path that will be generated in email etc if your path url is "127.0.0.1/reset-password/verify-token", client will receive email with url for verification -> "http://127.0.0.1/reset-password/verify?token=%s&email=%s".
 - POST `/reset-password/verification/verify , Object.of(String email, String tooken)` Returns true or false.
 - PUT `/reset-password/{email} , Object.of(String newPassword)` Password must be at least 5 symbols!



