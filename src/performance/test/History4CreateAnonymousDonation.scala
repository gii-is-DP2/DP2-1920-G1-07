package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class History4CreateAnonymousDonation extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9,und;q=0.8")
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
		.pause(8)
		
	}

	object Loggin {
		val loggin = exec(http("Loggin")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2)))
		.pause(7)
		
	}

	object Logged {
		val logged = exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "admin1")
			.formParam("password", "4dm1n")
			.formParam("_csrf", "bcc5a295-a303-4933-8523-d85be637b58b"))
		.pause(10)
		
	}

	object ShowCause{
		val showCause=exec(http("ShowCause")
			.get("/cause")
			.headers(headers_0))
		.pause(11)
		
	}

	object ShowDonation{
		val showDonation = exec(http("ShowDonation")
			.get("/cause/3/donations")
			.headers(headers_0))
		.pause(11)
		
	}

	object NewDonationForm{
		val newDonationForm= exec(http("NewDonationFrom")
			.get("/cause/3/donations/new")
			.headers(headers_0))
		.pause(19)
		
	}

	object NewDonation{
		val newDonation=exec(http("NewDonation")
			.post("/cause/3/donations/new")
			.headers(headers_3)
			.formParam("money", "1000")
			.formParam("anonymous", "true")
			.formParam("id", "")
			.formParam("_csrf", "651bd1a6-8ffb-44bc-9420-f73ea91a8449"))
		.pause(11)

	}
	val newDonationScn = scenario("CreateDonationTest").exec(Home.home,
												  Loggin.loggin,
												  Logged.logged,
												  ShowCause.showCause,
												  ShowDonation.showDonation,
												  NewDonationForm.newDonationForm,
												  NewDonation.newDonation
	)


	setUp(newDonationScn.inject(rampUsers(20000) during (100 seconds)))
	.protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95)
	)
}