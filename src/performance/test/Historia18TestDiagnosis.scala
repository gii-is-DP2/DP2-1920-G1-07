package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class Historia18TestDiagnosis extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

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

	val headers_11 = Map(
		"Proxy-Connection" -> "Keep-Alive",
		"User-Agent" -> "Microsoft-WNS/10.0")

    val uri2 = "http://tile-service.weather.microsoft.com/es-ES/livetile/preinstall"

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(8)
	}

	object LoginWithDiagnosis {
		val loginWithDiagnosis = exec(
        http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
        ).pause(8)
        .exec(
            http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "vet2")
			.formParam("password", "12345")
			.formParam("_csrf", "${stoken}")
        ).pause(8)
	}


	object LoginWithoutDiagnosis {
		val loginWithoutDiagnosis = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
        ).pause(8)
        .exec(
            http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "vet5")
			.formParam("password", "pablo")
			.formParam("_csrf", "${stoken}")
        ).pause(8)
	}


	object ShowMyDiagnosis {
		val showMyDiagnosis = exec(http("ShowMyDiagnosis")
			.get("/vets/mySpace")
			.headers(headers_0))
		.pause(57)
	}

	val vetWithDiagnosisScn = scenario("VetWithDiagnosis").exec(Home.home,
												LoginWithDiagnosis.loginWithDiagnosis,
												ShowMyDiagnosis.showMyDiagnosis)

	val vetWithoutDiagnosisScn = scenario("VetWithoutDiagnosis").exec(Home.home,
												LoginWithoutDiagnosis.loginWithoutDiagnosis,
												ShowMyDiagnosis.showMyDiagnosis)

/*
    setUp(
		vetWithDiagnosisScn.inject(atOnceUsers(90000)),
		vetWithoutDiagnosisScn.inject(atOnceUsers(90000))
		).protocols(httpProtocol)	
         */

	setUp(
		vetWithDiagnosisScn.inject(rampUsers(7000) during (100 seconds)),
		vetWithoutDiagnosisScn.inject(rampUsers(7000) during (100 seconds))
		).protocols(httpProtocol)
        .assertions(
            global.responseTime.max.lt(5000),
            global.responseTime.mean.lt(1000),
            global.successfulRequests.percent.gt(95)
        )
       
}