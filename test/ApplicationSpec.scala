import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import org.specs2.matcher.JsonMatchers
import play.api.libs.json.{Json, JsValue, JsArray}
import models.Task
/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification with JsonMatchers{

  "Application" should {

    "send json with all tasks list on / request " in {
      running(FakeApplication()) {
         val Some(root) = route(FakeRequest(GET, "/"))

         status(root) must equalTo(OK)
         contentType(root) must beSome.which(_ == "application/json") 
      }
    }

    "send json with all guest tasks list on /tasks request " in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
         // Añadimos un task a los 3 existentes en la evolucion para comprobar que lo contempla
         val newTask = Task.create("nuevo task", "guest")
         val Some(guestTasks) = route(FakeRequest(GET, "/tasks"))

         status(guestTasks) must equalTo(OK)
         contentType(guestTasks) must beSome.which(_ == "application/json")

         val resultJson = contentAsJson(guestTasks)
         val numGuestTasks = resultJson match {
            case array: JsArray => array.value.length
            case _ => None
         }
         numGuestTasks must equalTo(4)

         val resultString = Json.stringify(resultJson) 

         resultString must /#(0)/("userid" -> 0)
         resultString must /#(1)/("userid" -> 0)
         resultString must /#(2)/("userid" -> 0)
         resultString must /#(3)/("userid" -> 0)
         resultString must /#(0)/("username" -> "guest")
         resultString must /#(1)/("username" -> "guest")
         resultString must /#(2)/("username" -> "guest")
         resultString must /#(3)/("username" -> "guest")
      }
    }

    "send json with the data of task id requested on /tasks/{id}  " in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
         val Some(newTask) = Task.create("nuevo task 2", "dmcc")

         val Some(guestTasks) = route(FakeRequest(GET, "/tasks/"+newTask.id))

         status(guestTasks) must equalTo(OK)
         contentType(guestTasks) must beSome.which(_ == "application/json")

         val resultJson : JsValue = contentAsJson(guestTasks)
         val resultString = Json.stringify(resultJson) 

         resultString must /("userid" -> 2)
         resultString must /("username" -> "dmcc")
         resultString must /("label" -> "nuevo task 2")
      }
    }

    "send json with the data of task id requested on /tasks/{id}" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
         val Some(newTask) = Task.create("nuevo task 2", "dmcc")

         val Some(dmccTask) = route(FakeRequest(GET, "/tasks/"+newTask.id))

         status(dmccTask) must equalTo(OK)
         contentType(dmccTask) must beSome.which(_ == "application/json")

         val resultJson : JsValue = contentAsJson(dmccTask)
         val resultString = Json.stringify(resultJson) 

         resultString must /("userid" -> 2)
         resultString must /("username" -> "dmcc")
         resultString must /("label" -> "nuevo task 2")
      }
    }

    "send 404 NotFound if id on /tasks/{id} doesn't exists" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

         val Some(newTask) = route(FakeRequest(GET, "/tasks/"+99))

         status(newTask) must equalTo(NOT_FOUND)
      }
    }

    "send Created with the new task on json on request POST /tasks" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
         val Some(newTask) = route(
            FakeRequest(POST, "/tasks").withFormUrlEncodedBody(("label","nuevo task"))
         )

         status(newTask) must equalTo(CREATED)
         contentType(newTask) must beSome.which(_ == "application/json")

         val resultJson : JsValue = contentAsJson(newTask)
         val resultString = Json.stringify(resultJson) 

         resultString must /("userid" -> 0)
         resultString must /("username" -> "guest")
         resultString must /("label" -> "nuevo task")
      }
    }

    "send OK on request DELETE /tasks/{id} if the task was deleted" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val Some(newTask) = Task.create("nuevo task 3", "dmcc")

        val Some(dmccTask) = route(FakeRequest(DELETE, "/tasks/"+newTask.id))

        status(dmccTask) must equalTo(OK)

        Task.read(newTask.id) must equalTo(None)
      }
    }

  }
}
