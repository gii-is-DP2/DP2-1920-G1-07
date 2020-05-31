package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class Historia21Test extends Simulation {

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
		.pause(8)
	}

		object LoginAdmin {
			val loginAdmin = exec(
        http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			).pause(8)
        .exec(
            http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "admin1")
			.formParam("password", "4dm1n")
			.formParam("_csrf", "${stoken}")
        ).pause(8)
	}

		object VetList {
			val vetList = exec(http("VetList")
			.get("/vets/admin")
			.headers(headers_0))
		.pause(10)
		}
		
		object CreateVetForm {
			val createVetForm = exec(
				http("CreateVetForm")
			.get("/vets/create")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(8)
		.exec(
			http("VetCreated")
			.post("/vets/create")
			.headers(headers_3)
			.formParam("firstName", "Pablo")
			.formParam("lastName", "Reneses Garrido")
			.formParam("specialties", "1")
			.formParam("_specialties", "1")
			.formParam("user.username", "pa")
			.formParam("user.password", "pa")
			.formParam("_csrf", "${stoken}")
		).pause(13)
		}

		object CreateNotVetForm {
			val createNotVetForm = exec(http("CreateNotVetForm")
			.get("/vets/create")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(8)
		.exec(http("VetNotCreated")
			.post("/vets/create")
			.headers(headers_3)
			.formParam("firstName", "Pablo")
			.formParam("lastName", "Reneses Garrido")
			.formParam("specialties", "1")
			.formParam("_specialties", "1")
			.formParam("user.username", "")
			.formParam("user.password", "pa")
			.formParam("_csrf", "${stoken}")
		).pause(11)
		}
	val vetCreatedScn = scenario("VetCreatedScn").exec(Home.home,
												LoginAdmin.loginAdmin,
												VetList.vetList,
												CreateVetForm.createVetForm)

	val  vetNotCreatedScn = scenario("VetNotCreatedScn").exec(Home.home,
												LoginAdmin.loginAdmin,
												VetList.vetList,
												CreateNotVetForm.createNotVetForm)

/*
    setUp(
		vetWithDiagnosisScn.inject(atOnceUsers(90000)),
		vetWithoutDiagnosisScn.inject(atOnceUsers(90000))
		).protocols(httpProtocol)	
         */

	setUp(
		vetCreatedScn.inject(rampUsers(6000) during (100 seconds)),
		vetNotCreatedScn.inject(rampUsers(6000) during (100 seconds))
		).protocols(httpProtocol)
        .assertions(
            global.responseTime.max.lt(5000),
            global.responseTime.mean.lt(1000),
            global.successfulRequests.percent.gt(95)
        )
}