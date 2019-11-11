import javax.inject._

import play.api.http.DefaultHttpErrorHandler
import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import play.api.routing.Router
import scala.concurrent._

@Singleton
class ErrorHandler @Inject()(
                              env: Environment,
                              config: Configuration,
                              sourceMapper: OptionalSourceMapper,
                              router: Provider[Router]
                            ) extends DefaultHttpErrorHandler(env, config, sourceMapper, router) {

  override def onProdServerError(request: RequestHeader, exception: UsefulException): Future[Result] = {
    Future.successful(
      InternalServerError("A prod server error occurred: " + exception.getMessage)
    )
  }

  override def onDevServerError(request: RequestHeader, exception: UsefulException): Future[Result] = {
    Future.successful(
      InternalServerError("A dev server error occurred: " + exception.getMessage)
    )
  }

  override def onForbidden(request: RequestHeader, message: String): Future[Result] = {
    Future.successful(
      Forbidden(s"You're not allowed to access this resource. $message")
    )
  }

  override def onNotFound(request: RequestHeader, message: String): Future[Result] = {
    Future.successful(
      NotFound(s"Not found. $message")
    )
  }

  override def onOtherClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    Future.successful {
      println("onOtherClientError")
      new Status(statusCode)(s"Other Client Error. $message")
    }
  }

  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    Future.successful {
      println("onClientError")
      new Status(statusCode)(s"Client Error. $message")
    }
  }
}