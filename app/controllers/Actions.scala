package controllers

import java.util.Base64

import javax.inject.{Inject, Singleton}
import play.api.Logging
import play.api.mvc.Results._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class UserRequest[A](val username: Option[String], request: Request[A]) extends WrappedRequest[A](request)

class UserRequestAction @Inject()(val parser: BodyParsers.Default)(implicit val executionContext: ExecutionContext)
  extends ActionBuilder[UserRequest, AnyContent] with ActionTransformer[Request, UserRequest] {
  def transform[A](request: Request[A]): Future[UserRequest[A]] = Future.successful {
    new UserRequest(request.session.get("username"), request)
  }
}

class IsLoggedInAction @Inject()(implicit val executionContext: ExecutionContext) extends ActionFilter[UserRequest] {
  override protected def filter[A](request: UserRequest[A]): Future[Option[Result]] = {
    Future.successful {
      request.username match {
        case Some(_) => None
        case None => Option(Forbidden(views.html.defaultpages.unauthorized()(request)))
      }
    }
  }
}

class AuthenticatedUserAction @Inject()(parser: BodyParsers.Default)(implicit ec: ExecutionContext)
  extends ActionBuilderImpl(parser)  with Logging {

  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
    val maybeUsername = request.session.get("username")
    maybeUsername.map { _ =>
      block(new UserRequest(maybeUsername, request))
    }.getOrElse(
      Future.successful(Forbidden(views.html.defaultpages.unauthorized()(request)))
      )
  }
}