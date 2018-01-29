package oc.auth

import akka.http.scaladsl.server.Route
import oc.common.entities.RegistrationData
import oc.interfaces.Controller
import oc.users.UserService
import org.json4s.jackson.Serialization
import org.json4s.{DefaultFormats, jackson}

import scala.concurrent.Future


class AuthController(userService: UserService[Future]) extends Controller {

  import de.heikoseeberger.akkahttpjson4s.Json4sSupport._
  implicit val serialization: Serialization.type = jackson.Serialization // or native.Serialization
  implicit val formats: DefaultFormats.type = DefaultFormats

  override def route: Route = pathPrefix("users") {
    pathEndOrSingleSlash {
      register
    }
  }

  private def register = {
    (post & entity(as[RegistrationData])) { registrationData =>
      complete(userService.registerUser(registrationData))
    }
  }

}
