package test

import org.specs2.mutable._  
import play.api.test._  
import play.api.test.Helpers._

import models.User

class UserSpec extends Specification {

    def dateIs(date: java.util.Date, str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date) == str  
    def strToDate(str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(str)

    "User Model" should {
      "be retrieved by id" in {
          running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
            val Some(user) = User.read(2)
            user.id must equalTo(2)
            user.username must equalTo("dmcc")
          }
      }

      "be created" in {
         running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
             val Some(user) = User.create("usuarioNuevo")

             val Some(readUser) = User.read(user.id)

             readUser.id must equalTo(user.id)
             readUser.username must equalTo(user.username)
         }
      }

      "be deleted by id" in {
         running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
             val Some(user) = User.create("usuarioNuevo2")

             val rows = User.delete(user.id)
             User.read(user.id) must equalTo(None)
             rows must equalTo(1)
          }
      }

      "be checked if exists" in {
         running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
            val Some(user) = User.create("usuarioNuevo2")
            User.exists(user.username) must equalTo(true)
         }
      }
    }
 }