package models
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Task(id: Long, label: String, userid: Long)

object Task {
   val task = {
      get[Long]("id") ~ 
      get[String]("label") ~
      get[Long]("userid") map {
       case id~label~userid => Task(id, label, userid)
      }
   }
  
   def all(): List[Task] = DB.withConnection { implicit c =>
      SQL("select * from task where userid=0").as(task *) // TODO mostrar notfound?
   }
  
   def create(label: String) : Option[Task] = {
      DB.withConnection { implicit c =>
       SQL("insert into task (label, userid) values ({label},0)").on(
         'label -> label
       ).executeInsert()
      } match {
        case Some(id) => read(id)
        case _ => None // fallo al insertar?
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
        SQL("select * from task where id = {id} and userid=0").on(
          'id -> id
          ).as(Task.task.singleOpt)
      }
   }
}