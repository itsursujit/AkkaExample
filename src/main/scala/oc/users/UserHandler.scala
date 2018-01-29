package oc.users

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.pipe
import oc.redis.Repo

import scala.concurrent.ExecutionContextExecutor

object UserHandler {
  def props(db: Repo): Props = Props(new UserHandler(db))

  case class User(username: String, details: String)
  case class Register(username: String, password: String)
  case class Update(username: String, details: String)
  case class GetUser(username: String)
  case class DeleteUser(username: String)
  case class UserNotFound(username: String)
  case class UserDeleted(username: String)

}

class UserHandler(db: Repo) extends Actor with ActorLogging {
  import UserHandler._
  implicit val ec: ExecutionContextExecutor = context.dispatcher
  override def receive: Receive = {
    case Register(id, pwd) =>
      db.upsert(id, pwd) pipeTo sender()

    case Update(id, details) =>
      db.upsert(id, details) pipeTo sender()

    case GetUser(uname) =>
      //closing over the sender in Future is not safe
      //http://helenaedelson.com/?p=879
      val _sender = sender()
      db.get(uname).foreach {
        case Some(i) => _sender ! User(uname, i)
        case None => _sender ! UserNotFound
      }

    case DeleteUser(uname) =>
      val _sender = sender()
      db.del(uname).foreach {
        case i if i > 0 => _sender ! UserDeleted(uname)
        case _ => _sender ! UserNotFound(uname)
      }
  }

}

