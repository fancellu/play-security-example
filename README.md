# play-security-example

A couple of examples of play endpoint security, basic authentication and form based

## To run

`sbt run` or `sbt runProd` to run in prod mode in place

Point browser at localhost:9000

GET         /                     controllers.HomeController.index

### https://en.wikipedia.org/wiki/Basic_access_authentication
### https://tools.ietf.org/html/rfc7235#section-3.1

### (name is admin, password is admin)
GET         /basic                controllers.HomeController.basic
### (name is admin, password is admin)
GET         /basic2               controllers.HomeController.basic2

### Form based login (name is admin, password is admin)

GET         /login                controllers.LoginController.login
POST        /login                controllers.LoginController.tryLogin
GET         /logout               controllers.LoginController.logout

GET         /loggedin             controllers.LoginController.loggedIn
GET         /loggedin2            controllers.LoginController.loggedIn2
GET         /maybeloggedin        controllers.LoginController.maybeLoggedIn