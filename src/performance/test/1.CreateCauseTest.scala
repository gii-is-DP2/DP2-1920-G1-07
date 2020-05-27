package dp2Pruebas

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class CreateCauseTest extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.ico""", """.*.css""", """.*.png""", """.*.js"""), WhiteList())
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9",
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_5 = Map(
		"Proxy-Connection" -> "Keep-Alive",
		"User-Agent" -> "Microsoft-WNS/10.0")


	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(7)
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2)))
		.pause(12)
	}

	object Logged {
		val logged = exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "admin1")
			.formParam("password", "4dm1n")
			.formParam("_csrf", "eae8ca2f-236a-4c44-9610-faa45671e85d"))
		.pause(10)
	}

	object SeeAcceptedCauses {
		val seeAcceptedCauses = exec(http("SeeAcceptedCauses")
			.get("/cause")
			.headers(headers_0))
		.pause(6)
	}

	object InitFormCreateCause {
		val initFormCreateCause = exec(http("InitFormCreateCause")
			.get("/cause/new")
			.headers(headers_0))
		.pause(17)
	}

	object CauseCreated {
		val causeCreated = exec(http("CauseCreated")
			.post("/cause/new")
			.headers(headers_3)
			.formParam("title", "Pruebas")
			.formParam("description", "Esto es una prueba")
			.formParam("money", "10000.00")
			.formParam("deadline", "2020/07/25")
			.formParam("_csrf", "423d28e3-fd61-4292-9199-6ea57ac8db07"))
		.pause(10)
	}

	val scn = scenario("CreateCauseTest").exec(Home.home,
												Login.login,
												Logged.logged,
												SeeAcceptedCauses.seeAcceptedCauses,
												InitFormCreateCause.initFormCreateCause,
												CauseCreated.causeCreated
												)


	setUp(scn.inject(rampUsers(10000) during (100 seconds))).protocols(httpProtocol)
		.assertions(
            global.responseTime.max.lt(5000),
            global.responseTime.mean.lt(1000),
            global.successfulRequests.percent.gt(95)
        )

/*
	setUp(scn.inject(rampUsers(15000) during (100 seconds))).protocols(httpProtocol)
		.assertions(
            global.responseTime.max.lt(5000),
            global.responseTime.mean.lt(1000),
            global.successfulRequests.percent.gt(95)
        )
*/
}