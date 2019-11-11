package controllers

import javax.inject.Inject
import models.{User, UserDb}
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._

class LoginController @Inject()(
                                cc: MessagesControllerComponents,
                                userDb: UserDb,
                                authenticatedUserAction: AuthenticatedUserAction,
                                userRequestAction: UserRequestAction,
                                isLoggedInAction: IsLoggedInAction
                              ) extends MessagesAbstractController(cc) {

  val form: Form[User] = Form (
    mapping(
      "username" -> nonEmptyText(minLength = 3, maxLength = 24),
      "password" -> nonEmptyText(minLength = 4, maxLength = 24)
    )(User.apply)(User.unapply)
  )

  private val formSubmitUrl = routes.LoginController.tryLogin

  def login = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.login(form, formSubmitUrl))
  }

  def tryLogin = Action { implicit request: MessagesRequest[AnyContent] =>

    val formValidationResult: Form[User] = form.bindFromRequest
    formValidationResult.fold(
      formWithErrors =>
        BadRequest(views.html.login(formWithErrors, formSubmitUrl)),
      user=> {
          val foundUser = userDb.userValid(user)
          if (foundUser) {
            Redirect(routes.LoginController.loggedIn)
              .flashing("info" -> "You are logged in.").withSession("username" -> user.username)
          } else {
            Redirect(routes.LoginController.login).flashing("error" -> "Invalid username/password.")
          }
      }
    )
  }

  private val logoutUrl = routes.LoginController.logout

  def loggedIn() = authenticatedUserAction { implicit request: Request[AnyContent] =>
    println(request)
    Ok(views.html.loggedIn(logoutUrl))
  }

  def maybeLoggedIn() = userRequestAction { implicit request: UserRequest[AnyContent] =>
    println(request.username)
    Ok(s"username=${request.username}")
  }

  // we are sure we are logged in, request.username will always be Some
  def loggedIn2() = userRequestAction.andThen(isLoggedInAction) { implicit request: UserRequest[AnyContent] =>
    println(request.username)
    Ok(views.html.loggedIn(logoutUrl, request.username.getOrElse("")))
  }

  def logout = authenticatedUserAction { implicit request: Request[AnyContent] =>
    Redirect(routes.LoginController.login)
      .flashing("info" -> "You are logged out.").withNewSession
  }
}
