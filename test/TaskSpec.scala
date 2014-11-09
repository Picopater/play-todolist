package test

import org.specs2.mutable._  
import play.api.test._  
import play.api.test.Helpers._

import models.Task

class TaskSpec extends Specification {

    def dateIs(date: java.util.Date, str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date) == str  
    def strToDate(str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(str)

    "Task Model" should {
        "be retrieved by id" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
            val Some(task) = Task.read(1)
            task.userid must equalTo(Some(0))
            task.username must equalTo("guest")
            task.label must equalTo("guest task 1")
            task.endate must equalTo(None)
          }
        }

        "be created without endate" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                val Some(task) = Task.create("specs2 Task","guest")

                val Some(readTask) = Task.read(task.id)

                readTask.userid must equalTo(task.userid)
                readTask.label must equalTo(task.label)
                readTask.endate must equalTo(task.endate)
                readTask.username must equalTo(task.username)
            }
        }

        "be created with endate" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                val Some(task) = Task.create("specs2 Task","guest",Some(strToDate("2014-11-10")))
                
                val Some(readTask) = Task.read(task.id)

                readTask.userid must equalTo(task.userid)
                readTask.label must equalTo(task.label)
                readTask.endate must equalTo(task.endate)
                readTask.username must equalTo(task.username)
            }
        }

        "be deleted by id" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                val Some(task) = Task.create("specs2 Task 2","guest")

                val rows = Task.delete(task.id)
                Task.read(task.id) must equalTo(None)
                rows must equalTo(1)
          }
        }

        "be updated(only endate) by id" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                val Some(task) = Task.create("specs2 Task 3","guest",Some(strToDate("2014-11-10")))

                val rows = Task.update(task.id, strToDate("2014-11-12"))

                val Some(updatedTask) = Task.read(task.id)
                
                updatedTask.userid must equalTo(task.userid)
                updatedTask.label must equalTo(task.label)
                updatedTask.endate must not equalTo(None)
                updatedTask.endate must not equalTo(Some(strToDate("2014-11-10")))
                updatedTask.endate must equalTo(Some(strToDate("2014-11-12")))
                updatedTask.username must equalTo(task.username)
                rows must equalTo(1)
          }
        }
    }  
}