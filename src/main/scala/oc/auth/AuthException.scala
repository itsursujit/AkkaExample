package oc.auth

case class AuthException(e: Throwable) extends RuntimeException(e)
