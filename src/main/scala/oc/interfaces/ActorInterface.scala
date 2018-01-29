package oc.interfaces

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.stream.{ActorMaterializer, Materializer}

import scala.concurrent.ExecutionContextExecutor

trait ActorInterface extends ConfigInterface {

  implicit lazy val actorSystem: ActorSystem = ActorSystem("oc", config)

  implicit lazy val actorMaterializer: Materializer = ActorMaterializer()(actorSystem)

  implicit lazy val executionContext: ExecutionContextExecutor = actorSystem.dispatcher

  implicit val logger: LoggingAdapter

}
