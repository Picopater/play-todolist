package models
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Task(id: Long, label: String)

object Task {
   val task = {
      get[Long]("id") ~ 
      get[String]("label") map {
       case id~label => Task(id, label)
      }
   }
  
   def all(): List[Task] = DB.withConnection { implicit c =>
      SQL("select * from task").as(task *) // TODO mostrar notfound?
   }
  
   def create(label: String) : Option[Task] = {
      DB.withConnection { implicit c =>
       SQL("insert into task (label) values ({label})").on(
         'label -> label
       ).executeInsert()
      } match {
        case Some(id) => read(id)
        case _ => None // fallo al insertar?
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
        SQL("select * from task where id = {id}").on(
          'id -> id
          ).as(Task.task.singleOpt)
      }
   }
}