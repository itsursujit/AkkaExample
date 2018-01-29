package oc.common

import org.mindrot.jbcrypt.BCrypt

trait HashService {
  def hashPassword(password: String): String

  def checkPassword(password: String, hash: String): Boolean
}

class BCryptHashService extends HashService {

  def hashPassword(password: String): String =
    BCrypt.hashpw(password, BCrypt.gensalt())

  def checkPassword(password: String, hash: String): Boolean =
    BCrypt.checkpw(password, hash)

}

