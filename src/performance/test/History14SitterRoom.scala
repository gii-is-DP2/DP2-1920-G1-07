package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class History14SitterRoom extends Simulation {

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

  	object Login {
   		 val login = exec(
      			http("Login")
        		.get("/login")
        		.headers(headers_0)
       		 .check(css("input[name=_csrf]", "value").saveAs("stoken"))
    		).pause(20)
    		.exec(
    		  http("Logged")
        		.post("/login")
        		.headers(headers_3)
        		.formParam("username", "sitter1")
       		 .formParam("password", "sitter")        
       		 .formParam("_csrf", "${stoken}")
    		).pause(12)
 	 }
	object LoginAdmin {
   		 val loginAdmin = exec(
      			http("LoginAd")
        		.get("/login")
        		.headers(headers_0)
       		 .check(css("input[name=_csrf]", "value").saveAs("stoken2"))
    		).pause(2)
    		.exec(
    		  http("LoggedAd")
        		.post("/login")
        		.headers(headers_3)
        		.formParam("username", "admin1")
       		 .formParam("password", "4dm1n")        
       		 .formParam("_csrf", "${stoken2}")
    		).pause(14)
 	 }

	object Rooms {
		val rooms = exec(http("Rooms")
			.get("/sitter/rooms")
			.headers(headers_0))
		.pause(14)
	}

	object RoomsAd {
		val roomsAd = exec(http("Rooms")
			.get("/rooms")
			.headers(headers_0))
		.pause(14)
	}

	object AccessRoom {
		val accessRoom = exec(http("AccessRoom")
			.get("/rooms/1")
			.headers(headers_0))
		.pause(20)

	}

	object OtherRoom {
		val otherRoom = exec(http("OtherRoom")
			.get("/rooms/4")
			.headers(headers_0))
		.pause(10)

	}

	val roomSitter = scenario("Sitter").exec(Home.home, Login.login, Rooms.rooms, AccessRoom.accessRoom)
	val roomAdmin = scenario("Admin").exec(Home.home, LoginAdmin.loginAdmin, RoomsAd.roomsAd, OtherRoom.otherRoom)

	setUp(
		roomSitter.inject(rampUsers(600) during (120 seconds)),
		roomAdmin.inject(rampUsers(600) during (120 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}