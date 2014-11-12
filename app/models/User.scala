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

   def create(login: String) : Option[User] = {
      DB.withConnection { implicit c =>
         SQL("insert into usertask (username) values ({login})").on(
            'login -> login
          ).executeInsert()
      } match {
        case Some(id) => read(id)
        case _ => None // fallo db al insertar?
      }
   }

   def delete(id: Long): Int = {
      DB.withConnection { implicit c =>
       SQL("delete from usertask where id = {id}").on(
         'id -> id
       ).executeUpdate()
      }
   }
}