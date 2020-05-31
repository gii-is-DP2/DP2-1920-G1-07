package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class History19AdminRooms extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources()
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9,en;q=0.8")
		.doNotTrackHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(9)
	}

	object LoginAdmin {
   		 val loginAdmin = exec(
      			http("LoginAd")
        		.get("/login")
        		.headers(headers_0)
       		 .check(css("input[name=_csrf]", "value").saveAs("stoken"))
    		).pause(2)
    		.exec(
    		  http("LoggedAd")
        		.post("/login")
        		.headers(headers_3)
        		.formParam("username", "admin1")
       		 .formParam("password", "4dm1n")        
       		 .formParam("_csrf", "${stoken}")
    		).pause(14)
 	 }
	
	
	object Rooms {
		val rooms = exec(http("Rooms")
			.get("/rooms")
			.headers(headers_0))
		.pause(14)
	}
	object Room1 {
		val room1 = exec(http("Room1")
			.get("/rooms/1")
			.headers(headers_0))
		.pause(20)

	}
	
	object Room3 {
		val room3 = exec(http("room3")
			.get("/rooms/3")
			.headers(headers_0))
		.pause(10)

	}

	object ChangeSitter {
		val changeSitter = exec(http("ChangeSitter")
			.get("/rooms/3/sitter")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken2"))
		).pause(3)
		.exec(http("ChangeSitterPost")
			.post("/rooms/3/sitter")
			.headers(headers_3)
			.formParam("sitter", "15")
			.formParam("_csrf", "${stoken2}"))
		.pause(8)

	}
	
	val AccessRoomAdmin = scenario("AccessRoom").exec(Home.home, LoginAdmin.loginAdmin, Rooms.rooms, Room1.room1)
	val ChangeSitterAdmin = scenario("Sitter").exec(Home.home, LoginAdmin.loginAdmin, Rooms.rooms, Room3.room3, ChangeSitter.changeSitter)
	

	setUp(
		AccessRoomAdmin.inject(rampUsers(650) during (120 seconds)),
		ChangeSitterAdmin.inject(rampUsers(650) during (120 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}