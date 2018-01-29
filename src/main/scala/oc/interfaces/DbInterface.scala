package oc.interfaces

import cats.~>
import com.softwaremill.macwire._
import oc.common.DBIOTransformation
import slick.dbio.DBIO
import slick.jdbc.{JdbcBackend, JdbcProfile, MySQLProfile}

import scala.concurrent.Future

trait DbInterface {

  lazy val profile: JdbcProfile = MySQLProfile

  lazy val db: JdbcBackend.DatabaseDef = JdbcBackend.Database.forConfig("oc.db")

  lazy val dbioTransformation: DBIO ~> Future = wire[DBIOTransformation]

}
