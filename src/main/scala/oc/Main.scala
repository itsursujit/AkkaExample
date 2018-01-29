package oc


import akka.http.scaladsl.Http
import akka.http.scaladsl.util.FastFuture._
import com.typesafe.scalalogging.StrictLogging
import oc.gateway.ControllerGateway

object Main extends Dependencies with StrictLogging {

  def main(args: Array[String]): Unit = {
    Http().bindAndHandle(routes, httpInterface, httpPort).fast
      .map(binding => logger.info(s"RealWorld server started on ${binding.localAddress}"))
  }

  lazy val httpInterface: String = config.getString("http.interface")
  lazy val httpPort: Int = config.getInt("http.port")

}

trait Dependencies extends ControllerGateway