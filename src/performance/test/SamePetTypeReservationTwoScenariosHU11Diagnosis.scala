package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class CreateReservationTwoScenariosAlpha extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.png""", """.*.min.js""", """.*.ico"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_2 = Map("Origin" -> "http://www.dp2.com")
	object Home {
		val home = exec(http("request_0")
			.get("/"))
		.pause(8)
	} 

	object LoginFormOwner { 
		val loginFormOwner = exec(http("LoginFormOwner")
			.get("/login")
			.check(css("input[name=_csrf]","value").saveAs("stoken")))
		.pause(26)
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
			.get("/rooms"))
		.pause(11)
	}
	 object RoomDetails2 {
		 val roomDetails2 = exec(http("RoomDetails2")
			.get("/rooms/2"))
		.pause(26)
	 }
	 object GoodReservationCreated {
		 val goodReservation = exec(http("GoodReservationForm")
			.get("/rooms/2/reservations/new")
			.check(css("input[name=_csrf]","value").saveAs("stoken")))
		.pause(33)
		.exec(http("GoodReservationCreated")
			.post("/rooms/2/reservations/new")
			.headers(headers_2)
			.formParam("entryDate", "2021/06/24")
			.formParam("exitDate", "2021/07/09")
			.formParam("pet", "16")
			.formParam("ownerId", "")
			.formParam("petId", "")
			.formParam("_csrf", "${stoken}"))
		.pause(16)
	 }
	 object Room4Details {
		 val roomDestails4 = exec(http("Room4Details")
			.get("/rooms/4"))
		.pause(26)
	 }
	object BadReservationCreate {
		val badReservation = exec(http("BadReservationForm")
			.get("/rooms/4/reservations/new")
			.check(css("input[name=_csrf]","value").saveAs("stoken")))
		.pause(37)
		.exec(http("ReservationFailed")
			.post("/rooms/4/reservations/new")
			.headers(headers_2)
			.formParam("entryDate", "2021/05/10")
			.formParam("exitDate", "2021/05/26")
			.formParam("pet", "16")
			.formParam("ownerId", "")
			.formParam("petId", "")
			.formParam("_csrf", "${stoken}"))
		.pause(15)
	}

	object ReturnRoomDetails {
		val returnRoomDetails = exec(http("ReturnRoomDetails")
			.get("/rooms/4"))
		.pause(9)
	}

	val scnGoodReservation = scenario("CreateGoodReservation").exec(Home.home,
															LoginFormOwner.loginFormOwner,
															RoomsList.roomsList,
															RoomDetails2.roomDetails2,
															GoodReservationCreated.goodReservation,
															Home.home
													)

	val scnBadReservation = scenario("CreateBadReservation").exec(Home.home,
															LoginFormOwner.loginFormOwner,
															RoomsList.roomsList,
															Room4Details.roomDestails4,
															BadReservationCreate.badReservation,
															ReturnRoomDetails.returnRoomDetails
													)
	
	setUp(scnGoodReservation.inject(rampUsers(1700) during (150 seconds)),
		scnBadReservation.inject(rampUsers(1700) during (150 seconds))
		)
		.protocols(httpProtocol)
		.assertions(
			global.responseTime.max.lt(5000),
			global.responseTime.mean.lt(1000),
			global.successfulRequests.percent.gt(95)
		)
}