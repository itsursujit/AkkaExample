/*
package oc.redis


import akka.actor.{ActorRef, ActorSystem}
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes.{InternalServerError, NoContent, OK}
import akka.http.scaladsl.server.Directives.{as, authenticateBasicAsync, complete, delete, entity, get, logRequestResult, path, pathPrefix, post, put, _}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.Credentials
import akka.pattern.ask
import akka.stream.{ActorMaterializer, Materializer}
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}
import oc.common.entities.RegistrationData
import oc.interfaces.Controller
import oc.redis.UserHandler._
import oc.users.UserService
import org.json4s.{DefaultFormats, jackson}
import org.json4s.jackson.Serialization
import redis.RedisClient

import scala.concurrent.{ExecutionContextExecutor, Future}

case class UserPwd(pwd:String)

case class UpsertRequest(username:String, password:String )


trait Service {

  import scala.concurrent.duration._

  implicit val system: ActorSystem

  implicit def executor: ExecutionContextExecutor

  implicit val materializer: Materializer

  def config: Config

  val logger: LoggingAdapter

  def userHandler: ActorRef

  implicit def requestTimeout: Timeout = Timeout(5.seconds)

  def userAuthenticate(credentials: Credentials): Future[Option[UserPwd]] = {
    credentials match {
      case p@Credentials.Provided(userName) =>
        fetchUserId(userName).map {
          case Some(UserPwd(id)) if p.verify(id) =>
            Some(UserPwd(id))
          case _ => None

        }
      case _ =>
        Future.successful(None)
    }

  }

  def fetchUserId(userName: String): Future[Option[UserPwd]] = {

    (userHandler ? GetUser(userName)).map {
      case User(_, p) => Some(UserPwd(p))
      case _ => None
    }
  }
}

class RedisController(userHandler: ActorRef, userService: UserService[Future]) extends Controller {
  import de.heikoseeberger.akkahttpjson4s.Json4sSupport._
  implicit val serialization: Serialization.type = jackson.Serialization // or native.Serialization
  implicit val formats: DefaultFormats.type = DefaultFormats

  override def route: Route = {
    pathPrefix("api") {
      pathPrefix("user") {
        path("register") {
          post {
            entity(as[UpsertRequest]) { u =>
              complete {
                (userHandler ? UserHandler.Register(u.username, u.password)).map {
                  case true => OK -> s"Thank you ${u.username}"
                  case _ => InternalServerError -> "Failed to complete your request. please try later"
                }
              }
            }
          }
        } ~ logRequestResult("akka-http-secured-service") {
          pathEndOrSingleSlash {
            (post & entity(as[RegistrationData])) { registrationData =>
              complete(userService.registerUser(registrationData))
            }
          }
        }
      }
    }
  }
}


object AkkaHttpRedisService extends App with Service with ConcreteRedis {

  override implicit val system = ActorSystem()
  override implicit val executor = system.dispatcher
  override implicit val materializer = ActorMaterializer()
  val prodDb = new RedisRepoImpl {
    override def db = RedisClient(host = redisUrl.getHost, port = redisUrl.getPort, password = pwd)
  }


  override val config = ConfigFactory.load()
  override val logger = Logging(system, getClass)
  val userHandler = system.actorOf(UserHandler.props(prodDb))

  Http().bindAndHandle(route , config.getString("http.interface"), config.getInt("http.port"))
}
*/
