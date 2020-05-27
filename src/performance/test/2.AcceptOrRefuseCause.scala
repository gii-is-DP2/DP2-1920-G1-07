package dp2Pruebas

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class AcceptOrRefuseCause extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.ico""", """.*.css""", """.*.png""", """.*.js"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

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
		.pause(6)
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2)))
		.pause(8)
	}

	object Logged {
		val logged = exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "admin1")
			.formParam("password", "4dm1n")
			.formParam("_csrf", "31b8cd9a-cadc-4338-95e6-923b9bf88fa2"))
		.pause(7)
	}

	object SeeAcceptedCauses {
		val seeAcceptedCauses = exec(http("SeeAcceptedCauses")
			.get("/cause")
			.headers(headers_0))
		.pause(12)
	}

	object SeePendingCauses {
		val seePendingCauses = exec(http("SeePendingCauses")
			.get("/causes/PendingCauses")
			.headers(headers_0))
		.pause(14)
	}

	object InitFormEditStatus {
		val initFormEditStatus = exec(http("InitFormEditStatus")
			.get("/causes/PendingCauses/cause/1/edit")
			.headers(headers_0))
		.pause(19)
	}

	object AcceptCause {
		val acceptCause = exec(http("AcceptCause")
			.post("/causes/PendingCauses/cause/1/edit")
			.headers(headers_3)
			.formParam("status", "ACCEPTED")
			.formParam("_csrf", "83f15c65-dee6-4fdc-9b5a-7ee460354884"))
		.pause(11)
	}

	object RejectCause {
		val rejectCause = exec(http("RejectCause")
			.post("/causes/PendingCauses/cause/5/edit")
			.headers(headers_3)
			.formParam("status", "REJECTED")
			.formParam("_csrf", "83f15c65-dee6-4fdc-9b5a-7ee460354884"))
		.pause(6)
	}

	val scn = scenario("AcceptOrRefuseCause").exec(Home.home,
													Login.login,
													Logged.logged,
													SeeAcceptedCauses.seeAcceptedCauses,
													SeePendingCauses.seePendingCauses,
													InitFormEditStatus.initFormEditStatus,
													AcceptCause.acceptCause,
													InitFormEditStatus.initFormEditStatus,
													RejectCause.rejectCause
													)
	
	setUp(scn.inject(rampUsers(8000) during (100 seconds))).protocols(httpProtocol)
		.assertions(
            global.responseTime.max.lt(5000),
            global.responseTime.mean.lt(1000),
            global.successfulRequests.percent.gt(95)
        )

/*
	setUp(scn.inject(rampUsers(8500) during (100 seconds))).protocols(httpProtocol)
		.assertions(
            global.responseTime.max.lt(5000),
            global.responseTime.mean.lt(1000),
            global.successfulRequests.percent.gt(95)
        )
*/
}