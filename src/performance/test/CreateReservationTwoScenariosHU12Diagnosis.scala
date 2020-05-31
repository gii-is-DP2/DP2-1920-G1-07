package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class CreateReservationTwoScenariosHU12Diagnosis extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.png""", """.*.min.js""", """.*.ico"""), WhiteList())
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"Origin" -> "http://www.dp2.com",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_9 = Map(
		"Accept-Encoding" -> "gzip,deflate",
		"Proxy-Connection" -> "Keep-Alive",
		"User-Agent" -> "Apache-HttpClient/4.5.6 (Java/1.8.0_221)")

    val uri2 = "http://download.eclipse.org"

	object Home {
		val home = exec(http("request_0")
			.get("/"))
		.pause(8)
	} 

	object LoginFormOwner {
		val loginFormOwner = exec(http("LoginFormOwner")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken")))
		.pause(34)
		.exec(http("LoggedOwner")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "owner")
			.formParam("password", "owner")
			.formParam("_csrf", "${stoken}"))
		.pause(11)
	}
	
	object RoomsList { 
		val roomsList = exec(http("RoomsList")
			.get("/rooms")
			.headers(headers_0))
		.pause(11)
	}
	
	object RoomDetails2 {
		val roomDetails2 = exec(http("RoomDetails2")
			.get("/rooms/2")
			.headers(headers_0))
		.pause(12)
	}
	object RoomDetails4 {
		val roomDetails4 = exec(http("RoomDetails4")
			.get("/rooms/4")
			.headers(headers_0))
		.pause(12)
	}
	object GoodReservationCreated {
		val goodReservation = exec(http("GoodReservationForm")
			.get("/rooms/2/reservations/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken")))
		.pause(42)
		.exec(http("GoodReservationCreated")
			.post("/rooms/2/reservations/new")
			.headers(headers_2)
			.formParam("entryDate", "2021/01/01")
			.formParam("exitDate", "2021/01/02")
			.formParam("pet", "16")
			.formParam("ownerId", "")
			.formParam("petId", "")
			.formParam("_csrf", "${stoken}"))
		.pause(45)
	}

	object BadReservationForm {
		val badReservation = exec(http("BadReservationForm")
			.get("/rooms/4/reservations/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken")))
		.pause(33)
		.exec(http("BadReservation")
			.post("/rooms/4/reservations/new")
			.headers(headers_2)
			.formParam("entryDate", "2020/05/18")
			.formParam("exitDate", "2020/05/20")
			.formParam("pet", "17")
			.formParam("ownerId", "")
			.formParam("petId", "")
			.formParam("_csrf", "${stoken}"))
		.pause(54)
	}
	object ReturnRoomDetails {
		val returnRoomDetails = exec(http("ReturnRoomDetails")
			.get("/rooms/4"))
		.pause(9)
	}

	val scnGoodReservation = scenario("CreateReservationTwoScenariosHU12").exec(Home.home,
															LoginFormOwner.loginFormOwner,
															RoomsList.roomsList,
															RoomDetails2.roomDetails2,
															GoodReservationCreated.goodReservation,
															Home.home
													)

	val scnBadReservation = scenario("FailReservationTwoScenariosHU12").exec(Home.home,
															LoginFormOwner.loginFormOwner,
															RoomsList.roomsList,
															RoomDetails4.roomDetails4,
															BadReservationForm.badReservation,
															ReturnRoomDetails.returnRoomDetails
													)

	setUp(scnGoodReservation.inject(rampUsers(2000) during (150 seconds)),
		scnBadReservation.inject(rampUsers(2000) during (150 seconds))
		)
		.protocols(httpProtocol)
		.assertions(
			global.responseTime.max.lt(5000),
			global.responseTime.mean.lt(1000),
			global.successfulRequests.percent.gt(95)
		)
}