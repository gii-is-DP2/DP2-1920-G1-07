package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class AcceptReservationTwoScenariosHU13Diagnosis extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.png""", """.*.min.js""", """.*.ico"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Origin" -> "http://www.dp2.com",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_8 = Map("Accept" -> "image/webp,*/*")

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(6)
	}
	object LogginOwnerForm {
		val logginOwner = exec(http("LogginForm")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken")))
		.pause(18)
		.exec(http("LoggedOwnerAmine")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "amine")
			.formParam("password", "12345")
			.formParam("_csrf", "${stoken}"))
		.pause(12)
	}
	object RoomsList { 
		val roomsList= exec(http("RoomsList")
			.get("/rooms")
			.headers(headers_0))
		.pause(18)
	}
	
	object LogginAdminForm {
		val logginAdmin = exec(http("LogginForm")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken")))
		.pause(23)
		.exec(http("LoggedAdmin")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "admin1")
			.formParam("password", "4dm1n")
			.formParam("_csrf", "${stoken}"))
		.pause(9)
	}
	object RoomDetails2 {
		val roomDetails2 = exec(http("RoomDetails2")
			.get("/rooms/2")
			.headers(headers_0))
		.pause(15)
	}
		object RoomDetails4 {
		val roomDetails4 = exec(http("RoomDetails4")
			.get("/rooms/4")
			.headers(headers_0))
		.pause(15)
	}
	object UpdatingReservation {
		val reservationRoom3Accepted = exec(http("UpdatingReservation")
			.get("/rooms/2/12/reservation/2/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken")))
		.pause(15)
		.exec(http("ReservationRoom2Accepted")
			.post("/rooms/2/12/reservation/2/edit")
			.headers(headers_2)
			.formParam("entryDate", "2021/03/31")
			.formParam("exitDate", "2021/04/02")
			.formParam("status", "ACCEPTED")
			.formParam("pet", "16")
			.formParam("roomId", "2")
			.formParam("ownerId", "12")
			.formParam("petId", "Pet Dog")
			.formParam("_csrf", "${stoken}"))
		.pause(50)
	}

	object DeteleReservationRoom4 {
		val room4ReservationDelete = exec(http("ReservationDeleted")
			.get("/rooms/4/reservation/6/delete")
			.headers(headers_0))
		.pause(12)
	}

	
	val scnAcceptReservation = scenario("AcceptReservationTwoScenariosHU13").exec(Home.home,
															LogginAdminForm.logginAdmin,
															RoomsList.roomsList,
															RoomDetails2.roomDetails2,
															UpdatingReservation.reservationRoom3Accepted
													)

	val scnDeleteReservation = scenario("DeleteReservationTwoScenariosHU13").exec(Home.home,
															LogginOwnerForm.logginOwner,
															RoomsList.roomsList,
															RoomDetails4.roomDetails4,
															DeteleReservationRoom4.room4ReservationDelete
															
													)
	
	setUp(scnAcceptReservation.inject(rampUsers(4000) during (150 seconds)),
		scnDeleteReservation.inject(rampUsers(4000) during (150 seconds))
		)
		.protocols(httpProtocol)
		.assertions(
			global.responseTime.max.lt(5000),
			global.responseTime.mean.lt(1000),
			global.successfulRequests.percent.gt(95)
		)
}