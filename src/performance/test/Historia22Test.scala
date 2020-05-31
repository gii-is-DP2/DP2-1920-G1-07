package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class Historia22Test extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9")
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
		.pause(7)
	}
	
		object Login {
		val login = exec(
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

		object MyVetSpace {
			val myVetSpace = exec(http("MyVetSpace")
			.get("/vets/mySpace")
			.headers(headers_0))
		.pause(10)
		}


		object AddDiagnosisForm {
			val addDiagnosisForm = exec(http("AddDiagnosisForm")
			.get("/vet/7/diagnosis?visitId=6")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(8)
		.exec(http("DiagnosisCreated")
			.post("/vet/7/diagnosis?visitId=6")
			.headers(headers_3)
			.formParam("description", "Todo bien")
			.formParam("date", "2020/10/06")
			.formParam("_csrf", "${stoken}")
		).pause(11)
		}
		
		object AddNotDiagnosisForm {
			val addNotDiagnosisForm = exec(http("AddNotDiagnosisForm")
			.get("/vet/7/diagnosis?visitId=6")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(8)
		.exec(http("DiagnosisCreated")
			.post("/vet/7/diagnosis?visitId=6")
			.headers(headers_3)
			.formParam("description", "Todo bien")
			.formParam("date", "2020/03/10")
			.formParam("_csrf", "${stoken}")
		).pause(6)
		}


		val diagnosisCreatedScn = scenario("DiagnosisCreatedScn").exec(Home.home,
																Login.login,
																MyVetSpace.myVetSpace,
																AddDiagnosisForm.addDiagnosisForm)

		val  diagnosisNotCreatedScn = scenario("DiagnosisNotCreatedScn").exec(Home.home,
																Login.login,
																MyVetSpace.myVetSpace,
																AddNotDiagnosisForm.addNotDiagnosisForm)
/*
    setUp(
		vetWithDiagnosisScn.inject(atOnceUsers(90000)),
		vetWithoutDiagnosisScn.inject(atOnceUsers(90000))
		).protocols(httpProtocol)	
         */

	setUp(
		diagnosisCreatedScn.inject(rampUsers(1000) during (150 seconds)),
		diagnosisNotCreatedScn.inject(rampUsers(1000) during (150 seconds))
		).protocols(httpProtocol)
        .assertions(
            global.responseTime.max.lt(5000),
            global.responseTime.mean.lt(1000),
            global.successfulRequests.percent.gt(95)
        )
		
	

	
}