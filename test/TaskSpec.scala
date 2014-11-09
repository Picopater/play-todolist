package test

import org.specs2.mutable._  
import play.api.test._  
import play.api.test.Helpers._

import models.Task

class TaskSpec extends Specification {

    //def dateIs(date: java.util.Date, str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date) == str  
    //def strToDate(str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(str)

    "Task Model" should {
        "be retrieved by id" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
            val Some(task) = Task.read(1)
            task.userid must equalTo(Some(0))
            task.label must equalTo("guest task 1")
            task.endate must equalTo(None)
          }
        }
    }  
}