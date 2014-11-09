package models
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.util.Date

case class Task(id: Long, label: String, userid: Option[Long], username: String, endate: Option[Date])

object Task {
   val task = {
      get[Long]("id") ~
      get[String]("label") ~
      get[Option[Long]]("userid") ~
      get[String]("username") ~
      get[Option[Date]]("endate") map {
       case id~label~userid~username~endate => Task(id, label, userid, username, endate)
      }
   }

   def all(): List[Task] = DB.withConnection { implicit c =>
      SQL("select task.*, usertask.username from task, usertask where task.userid=usertask.id").as(task *)
   }

   def all(login: String, endate: Option[Date] = None): List[Task] = DB.withConnection { implicit c =>
     endate match {
       case Some(date) => {
         val userid = SQL("select id from usertask where username={login}").on(
           'login -> login
         ).as(scalar[Long].singleOpt)
         SQL("select task.*, usertask.username  from task, usertask where userid={userid} and userid=usertask.id and endate={date}").on(
           'userid -> userid,
           'date -> date
         ).as(task *)
       }
       case None => {
         val userid = SQL("select id from usertask where username={login}").on(
           'login -> login
         ).as(scalar[Long].singleOpt)
         SQL("select task.*, usertask.username  from task, usertask where userid={userid} and userid=usertask.id").on(
           'userid -> userid
         ).as(task *)
       }
     }
   }
  

  def userExists(login: String): Boolean = {
    DB.withConnection { implicit c =>
      SQL("select count(*) from usertask where username = {login}").on(
            'login -> login).as(scalar[Long].single) == 1
    }
  }

  def create(label: String, login: String, endate: Option[Date]=None) : Option[Task] = {
      DB.withConnection { implicit c =>
      val userid = SQL("select id from usertask where username={login}").on(
          'login -> login
      ).as(scalar[Long].singleOpt)
      SQL("insert into task (label, userid, endate) values ({label},{userid},{endate})").on(
         'label -> label,
         'userid -> userid,
         'endate -> endate
       ).executeInsert()
      } match {
        case Some(id) => read(id)
        case _ => None // fallo db al insertar?
      }
   }
  
   def delete(id: Long): Int = {
      DB.withConnection { implicit c =>
       SQL("delete from task where id = {id}").on(
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

   //TODO incluir el editar otros campos
   def update(id: Long, endate: Date): Int = {
     DB.withConnection { implicit c =>
       SQL("update task set endate={newEndDate} where id = {id}").on(
                           'id -> id,
                           'newEndDate -> endate
                         ).executeUpdate()

     }
   }

  def allTasksWithDate(endate: Date): List[Task] = {
    DB.withConnection { implicit c =>
      SQL("select task.*, usertask.username  from task, usertask where userid=usertask.id and endate={endate}").on(
        'endate -> endate
      ).as(task *)
    }
  }

}