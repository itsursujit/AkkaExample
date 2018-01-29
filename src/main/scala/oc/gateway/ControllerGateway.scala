package oc.gateway

import akka.http.scaladsl.server.{Directives, Route}
import com.softwaremill.macwire._
import oc.auth.AuthController
import oc.interfaces.Controller
import oc.users.UserController
trait ControllerGateway extends ServiceGateway with Directives {

  lazy val authController: AuthController = wire[AuthController]

  lazy val userController: UserController = wire[UserController]

  lazy val controllers: Set[Controller] = wireSet[Controller]

  lazy val routes: Route = pathPrefix("api") {
    controllers.foldLeft[Route](reject)(_ ~ _.route)
  }
}
