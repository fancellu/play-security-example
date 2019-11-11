package controllers

import java.util.Base64

import javax.inject._
import play.api.mvc.Results.Unauthorized
import play.api.mvc.{Action, _}

import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action {
    Ok(views.html.index("Your new application is ready. No auth needed"))
  }

  // https://en.wikipedia.org/wiki/Basic_access_authentication

  def BasicSecuredAsync[A](username: String, password: String)(action: Action[A]): Action[A] = Action.async(action.parser) { implicit request =>
    val submittedCredentials: Option[List[String]] = for {
      authHeader <- request.headers.get("Authorization")
      parts <- authHeader.split(' ').drop(1).headOption
    } yield new String(Base64.getDecoder.decode(parts)).split(':').toList

    submittedCredentials.collect {
      case `username` :: `password` :: Nil =>action(request)
    }.getOrElse {
      Future.successful(Unauthorized(views.html.defaultpages.unauthorized()).withHeaders("WWW-Authenticate" -> """Basic realm="Secured Area""""))
    }
  }

  def basic = BasicSecuredAsync("admin", "password") {
    Action.async { implicit request =>
      Future.successful(Ok(views.html.index("You have passed basic authentication.")))
    }
  }

  def basic2 = BasicSecuredAsync("admin2", "password2") {
    Action.async { implicit request =>
      Future.successful(Ok(views.html.index("You have passed basic authentication. no2")))
    }
  }
}
