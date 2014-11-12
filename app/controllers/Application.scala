package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Task
import play.api.libs.json._
import play.api.libs.functional.syntax._
import java.util.Date


case class TaskData(label: String,  endate: Option[Date])

object Application extends Controller {

  val taskForm = Form(
    mapping(
    "label" -> nonEmptyText,
    "endate" -> optional(date("yyyy-MM-dd"))
    )(TaskData.apply)(TaskData.unapply)
  )

  val endateForm = Form(
      "endate" -> date("yyyy-MM-dd")
    )

  val dateWrite = Writes.dateWrites("yyyy-MM-dd")
  val formatter = new java.text.SimpleDateFormat("yyyy-MM-dd")

  implicit val taskWrites: Writes[Task] = (
    (JsPath \ "id").write[Long] and
    (JsPath \ "label").write[String] and
    (JsPath \ "userid").writeNullable[Long] and
    (JsPath \ "username").write[String] and
    (JsPath \ "endate").writeNullable[Date](dateWrite)
  )(unlift(Task.unapply))

  def index = Action {
    Ok(Json.toJson(Task.all()))
  }

  def tasks = Action {
    Ok(Json.toJson(Task.all("guest")))
  }

  def tasksUser(login: String, endate: Option[String]) = Action {
    val date = endate match {
      case Some(endate) => Some(formatter.parse(endate))
      case None => None
    }
    val list = Task.all(login, date)
    if(!list.isEmpty) {
      val json = Json.toJson(list)
      Ok(json)
    }
    else NotFound
  }

  def newTask = newTaskUser("guest")

  def newTaskUser(login: String) = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest("Error en la peticion"),
      formData => if (Task.userExists(login)) {
                    val result = Task.create(formData.label, login, formData.endate)
                    result match {
                      case Some(x) => Created(Json.toJson(result))
                      case None => NotFound("No encuentra el ultimo task que se inserto") // si falla al insertar?
                    }
                  }
                  else NotFound("Error: No existe el propietario de la tarea: " + login)
    )
  }

  def deleteTask(id: Long) = Action {
    if(Task.delete(id) > 0) Ok
    else NotFound
  }

  def task(id: Long) = Action {
    val result = Task.read(id)
    result match {
      case Some(x) => Ok(Json.toJson(result))
      case None => NotFound
    }
  }

  def dateTasks(endate: String) = Action {
    val date = formatter.parse(endate)
    val result = Task.allTasksWithDate(date)
    
    if(!result.isEmpty) {
      val json = Json.toJson(result)
      Ok(json)
    }
    else NotFound
  }

  // adecuar para editar todos los campos
  def updateTask(id: Long) = Action {
    implicit request =>
    endateForm.bindFromRequest.fold(
      errors => BadRequest("Error en la peticion"),
      newdate => {
        val result = Task.update(id, newdate)
        if(result>0) {
          val task = Task.read(id)
          task match {
            case Some(x) => Ok(Json.toJson(task))
            case None => NotFound
          }
        }
        else NotFound
      }
    )
  }
}