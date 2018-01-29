package oc.gateway

import akka.actor.ActorRef
import cats.instances.FutureInstances
import com.softwaremill.macwire._
import oc.auth.{AuthService, AuthServiceImpl}
import oc.common.services.{BCryptHashService, HashService}
import oc.interfaces.{ActorInterface, DBIOInstancesInterface}
import oc.users.{UserRegistryActor, UserService, UserServiceImpl}
import slick.dbio.DBIO

import scala.concurrent.Future

trait ServiceGateway extends DaoGateway with ActorInterface with FutureInstances with DBIOInstancesInterface {

  lazy val authService: AuthService = wire[AuthServiceImpl]

  lazy val userRegistryActor: ActorRef = actorSystem.actorOf(UserRegistryActor.props, "userRegistryActor")

  lazy val hashService: HashService = wire[BCryptHashService]

  lazy val userService: UserService[Future] = wire[UserServiceImpl[Future, DBIO]]
}
