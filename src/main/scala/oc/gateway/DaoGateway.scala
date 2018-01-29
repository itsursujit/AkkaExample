package oc.gateway

import com.softwaremill.macwire._
import oc.interfaces.{ActorInterface, DbInterface}
import oc.users.{UserDao, UserDaoImpl}
import slick.dbio.DBIO

trait DaoGateway extends DbInterface with ActorInterface {
  lazy val userDao: UserDao[DBIO] = wire[UserDaoImpl]
}
