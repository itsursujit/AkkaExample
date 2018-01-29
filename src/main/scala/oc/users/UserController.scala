package oc.users

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, concat, entity, onSuccess, pathEnd, pathPrefix, rejectEmptyResponse}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.{delete, get, post}
import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import oc.common.entities.RegistrationData
import oc.interfaces.{ActorInterface, Controller}
import oc.users.UserRegistryActor._
import org.json4s.jackson.Serialization
import org.json4s.{DefaultFormats, jackson}
import akka.pattern.ask


import scala.concurrent.Future


class UserController(userRegistryActor: ActorRef) extends Controller with ActorInterface {

  import de.heikoseeberger.akkahttpjson4s.Json4sSupport._
  implicit val serialization: Serialization.type = jackson.Serialization // or native.Serialization
  implicit val formats: DefaultFormats.type = DefaultFormats

  //#all-routes
  //#users-get-post
  //#users-get-delete
  override def route: Route = {
    pathPrefix("test") {
      concat(
        //#users-get-delete
        pathEnd {
          concat(
            get {
              val users: Future[Users] =
                (userRegistryActor ? GetUsers).mapTo[Users]
              complete(users)
            },
            post {
              entity(as[UserRegistery]) { user =>
                val userCreated: Future[ActionPerformed] =
                  (userRegistryActor ? CreateUser(user)).mapTo[ActionPerformed]
                onSuccess(userCreated) { performed =>
                  complete((StatusCodes.Created, performed))
                }
              }
            }
          )
        },
        //#users-get-post
        //#users-get-delete
        path(Segment) { name =>
          concat(
            get {
              //#retrieve-user-info
              val maybeUser: Future[Option[UserRegistery]] =
                (userRegistryActor ? GetUser(name)).mapTo[Option[UserRegistery]]
              rejectEmptyResponse {
                complete(maybeUser)
              }
              //#retrieve-user-info
            },
            delete {
              //#users-delete-logic
              val userDeleted: Future[ActionPerformed] =
                (userRegistryActor ? DeleteUser(name)).mapTo[ActionPerformed]
              onSuccess(userDeleted) { performed =>
                complete((StatusCodes.OK, performed))
              }
              //#users-delete-logic
            }
          )
        }
      )
      //#users-get-delete
    }
  }
}
