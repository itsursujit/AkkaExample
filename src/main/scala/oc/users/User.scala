package oc.users

import oc.interfaces.SlickInterface

case class User(email: String,
                username: String,
                passwordHash: String,
                bio: Option[String] = None,
                image: Option[String] = None)

trait UserModel extends SlickInterface {
  import profile.api._

  class Users(t: Tag) extends Table[User](t, "users") {
    def email = column[String]("email", O.PrimaryKey)
    def username = column[String]("username", O.Unique)
    def password = column[String]("password")
    def bio = column[String]("bio")
    def image = column[String]("image")
    def * = (email, username, password, bio.?, image.?) <> (User.tupled, User.unapply)
  }

  val users = TableQuery[Users]

}
