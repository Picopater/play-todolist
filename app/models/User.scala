package models
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.util.Date

case class User(id: Long, username: String)

object User {
   val user = {
      get[Long]("id") ~
      get[String]("username") map {
       case id~username => User(id, username)
      }
   }

   def read(id: Long): Option[User] = {
      DB.withConnection { implicit c =>
        SQL("select * from usertask where id = {id}").on(
          'id -> id
          ).as(User.user.singleOpt)
      }
   }
}