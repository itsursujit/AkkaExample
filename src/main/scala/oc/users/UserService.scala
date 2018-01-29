package oc.users

import cats.{Monad, ~>}
import oc.auth.AuthService
import cats.implicits._
import io.scalaland.chimney.dsl._
import oc.common.entities.{RegistrationData, UserData}
import oc.common.services.HashService

import scala.language.higherKinds

trait UserService[F[_]] {

  def registerUser(registrationData: RegistrationData): F[UserData]

}

class UserServiceImpl[F[_] : Monad, DB[_] : Monad](userDao: UserDao[DB],
                                                   hashService: HashService,
                                                   authService: AuthService,
                                                   db: DB ~> F) extends UserService[F] {

  override def registerUser(registrationData: RegistrationData): F[UserData] = {
    val user = User(
      email = registrationData.email,
      username = registrationData.username,
      passwordHash = hashService.hashPassword(registrationData.password)
    )
    db {
      userDao.create(user).map(_.into[UserData]
        .withFieldComputed(_.token, u => authService.createTokenByEmail(u.email))
        .transform
      )
    }
  }

}
