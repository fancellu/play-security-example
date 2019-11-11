package models

import javax.inject.{Inject, Singleton}

case class User(username: String, password: String)

@Singleton
class UserDb @Inject()() {
  def userValid(u: User): Boolean = u.username == "admin" && u.password == "password"
}