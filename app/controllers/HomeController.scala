package controllers

import java.util.Base64

import javax.inject._
import play.api.mvc.{Action, _}

import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, actiona: Actions) extends AbstractController(cc) {

  def index = Action {
    Ok(views.html.index("Your new application is ready. No auth needed"))
  }

  // https://en.wikipedia.org/wiki/Basic_access_authentication

  def basic = actiona.BasicSecuredAsync("admin", "password") {
    Action.async { implicit request =>
      Future.successful(Ok(views.html.index("You have passed basic authentication.")))
    }
  }

  def basic2 = actiona.BasicSecuredAsync("admin2", "password2") {
    Action.async { implicit request =>
      Future.successful(Ok(views.html.index("You have passed basic authentication. no2")))
    }
  }
}
