package oc.interfaces

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.stream.{ActorMaterializer, Materializer}
import akka.util.Timeout
import scala.concurrent.duration._

import scala.concurrent.ExecutionContextExecutor

trait ActorInterface extends ConfigInterface {

  implicit lazy val actorSystem: ActorSystem = ActorSystem("oc", config)

  implicit lazy val actorMaterializer: Materializer = ActorMaterializer()(actorSystem)

  implicit lazy val executionContext: ExecutionContextExecutor = actorSystem.dispatcher

  implicit def requestTimeout: Timeout = Timeout(5.seconds)

  //implicit val logger: LoggingAdapter

}
