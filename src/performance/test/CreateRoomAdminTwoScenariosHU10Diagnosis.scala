package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class CreateRoomAdminTwoScenariosDiagnosis extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.png""", """.*.min.js""", """.*.ico"""), WhiteList())
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_7 = Map(
		"Proxy-Connection" -> "Keep-Alive",
		"User-Agent" -> "Microsoft-WNS/10.0")

    val uri2 = "http://tile-service.weather.microsoft.com/es-ES/livetile/preinstall"

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(12)
	}
	object FormLogin{
		val formLogin = exec(http("FormLogin")
			.get("/login")
			.headers(headers_0)
            .check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(17)
	    .exec(http("LoggedAdmin")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "admin1")
			.formParam("password", "4dm1n")
			.formParam("_csrf", "${stoken}")
        ).pause(20)
	}
	object RoomsList {
		val roomsList = exec(http("RoomsList")
			.get("/rooms")
			.headers(headers_0))
		.pause(15)
	}
	object RoomCreated { 
		val roomCreated =exec(http("RoomCreateForm")
			.get("/rooms/new")
			.headers(headers_0)
            .check(css("input[name=_csrf]", "value").saveAs("stoken"))
        ).pause(26)
        .exec(http("RoomCreated")
			.post("/rooms/new")
			.headers(headers_3)
			.formParam("name", "Room")
			.formParam("capacity", "7")
			.formParam("type", "dog")
			.formParam("_csrf", "${stoken}")
        ).pause(69)
	}
	object ErrorCreatinBackForm { 
		val errorCreating_BackForm= exec(http("RoomCreateForm")
			.get("/rooms/new")
			.headers(headers_0)
            .check(css("input[name=_csrf]", "value").saveAs("stoken"))
        ).pause(26)
        .exec(http("ErrorCreating_BackForm")
			.post("/rooms/new")
			.headers(headers_3)
			.formParam("name", "Room1")
			.formParam("capacity", "4")
			.formParam("type", "bird")
			.formParam("_csrf", "${stoken}")
        ).pause(18)
	}
	val scnGoodRoom = scenario("CreateGoodRoom").exec(Home.home,
												FormLogin.formLogin,
												RoomsList.roomsList,
												RoomCreated.roomCreated)
	val scnBadRoom = scenario("CreateBadRoom").exec(Home.home,
												FormLogin.formLogin,
												RoomsList.roomsList,
												ErrorCreatinBackForm.errorCreating_BackForm)					

		

	setUp(
		scnGoodRoom.inject(rampUsers(4000) during (100 seconds)),
		scnBadRoom.inject(rampUsers(4000) during (100 seconds))
		).protocols(httpProtocol)
         .assertions(
            global.responseTime.max.lt(5000),
            global.responseTime.mean.lt(1000),
            global.successfulRequests.percent.gt(95)
         )
}