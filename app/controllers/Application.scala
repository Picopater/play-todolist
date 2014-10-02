package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Task
import play.api.libs.json._
import play.api.libs.functional.syntax._

object Application extends Controller {

  val taskForm = Form(
    "label" -> nonEmptyText
  )

  implicit val taskWrites: Writes[Task] = (
    (JsPath \ "id").write[Long] and
    (JsPath \ "label").write[String]
  )(unlift(Task.unapply))

  def index = Action {
   Ok(views.html.index(Task.all(), taskForm))
  }

  def tasks = Action {
    val list = Task.all
    if(!list.isEmpty) {
      val json = Json.toJson(list)
      Ok(json)
    }
    else NotFound
  }
  

  def newTask = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all(), errors)),
      label => {
        val result = Task.create(label)
        result match {
          case Some(x) => Created(Json.toJson(result))
          case None => NotFound("No encuentra el ultimo task que se inserto") // si falla al insertar?
        }
      }
    )
  }
  
  def deleteTask(id: Long) = Action {
    Task.delete(id)
    Redirect(routes.Application.tasks)
  }



  def task(id: Long) = Action {
    val result = Task.read(id)
    result match {
      case Some(x) => Ok(Json.toJson(result))
      case None => NotFound
    }
  }
}