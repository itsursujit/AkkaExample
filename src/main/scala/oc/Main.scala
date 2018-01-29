package oc


import java.io.IOException

import akka.http.scaladsl.Http
import akka.http.scaladsl.util.FastFuture._
import com.typesafe.scalalogging.StrictLogging
import oc.common.services.FlywayService
import oc.gateway.ControllerGateway

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Main extends App with Dependencies with StrictLogging {

  try
  {
    val flywayService = new FlywayService
    flywayService.migrateDatabaseSchema
    Http().bindAndHandle(routes, httpInterface, httpPort).fast
      .map(binding => logger.info(s"OC server started on ${binding.localAddress}"))


    lazy val httpInterface: String = config.getString("http.interface")
    lazy val httpPort: Int = config.getInt("http.port")

    Await.result(actorSystem.whenTerminated, Duration.Inf)
  }
  catch{
    case ioException: IOException => println(ioException.getMessage)
    case classNotFoundException: ClassNotFoundException => println(classNotFoundException.getMessage)
    case ex: Exception => println(ex.getMessage)
  }



}

trait Dependencies extends ControllerGateway
