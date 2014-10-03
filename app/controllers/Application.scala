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
    (JsPath \ "label").write[String] and
    (JsPath \ "userid").write[Long]
  )(unlift(Task.unapply))

  def index = Action {
   Ok(views.html.index(Task.all("guest"), taskForm))
  }

  def tasks(login: String) = Action {
    val list = Task.all(login)
    if(!list.isEmpty) { // TODO mostrar lista vacia o notfound??
      val json = Json.toJson(list)
      Ok(json)
    }
    else NotFound
  }

  def newTask(login: String) = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all("guest"), errors)),
      label => {
        val result = Task.create(label, login)
        result match {
          case Some(x) => Created(Json.toJson(result))
          case None => NotFound("No encuentra el ultimo task que se inserto") // si falla al insertar?
        }
      }
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
}