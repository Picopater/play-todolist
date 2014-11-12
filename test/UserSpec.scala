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
    }
 }