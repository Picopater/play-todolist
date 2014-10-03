package models
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Task(id: Long, label: String, userid: Long, username: String)

object Task {
   val task = {
      get[Long]("id") ~ 
      get[String]("label") ~
      get[Long]("userid") ~ 
      get[String]("username") map {
       case id~label~userid~username => Task(id, label, userid, username)
      }
   }

   def all(login: String): List[Task] = DB.withConnection { implicit c =>
      val userid = SQL("select id from usertask where username={login}").on(
          'login -> login
        ).as(scalar[Long].singleOpt)
      SQL("select task.*, usertask.username  from task, usertask where userid={userid} and userid=usertask.id").on(
        'userid -> userid
      ).as(task *) // TODO mostrar notfound?
   }
  
   def create(label: String, login: String) : Option[Task] = {
      DB.withConnection { implicit c =>
      val userid = SQL("select id from usertask where username={login}").on(
          'login -> login
      ).as(scalar[Long].singleOpt)
      SQL("insert into task (label, userid) values ({label},{userid})").on(
         'label -> label,
         'userid -> userid
       ).executeInsert()
      } match {
        case Some(id) => read(id)
        case _ => None // fallo db al insertar?
      }
   }
  
   def delete(id: Long): Int = {
      DB.withConnection { implicit c =>
       SQL("delete from task where id = {id} and userid=0").on(
         'id -> id
       ).executeUpdate()
      }
   }

   def read(id: Long): Option[Task] = {
      DB.withConnection { implicit c =>
        SQL("select task.*, usertask.username from task, usertask where task.id = {id} and task.userid=usertask.id").on(
          'id -> id
          ).as(Task.task.singleOpt)
      }
   }
}