package oc.interfaces

import akka.http.scaladsl.server.{Directives, Route}
import org.json4s.{DefaultFormats, jackson}
import org.json4s.jackson.Serialization

trait Controller extends Directives {

  def route: Route

}
