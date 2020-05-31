package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class Historia20Test extends Simulation {

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
			.formParam("username", "amine")
			.formParam("password", "12345")
			.formParam("_csrf", "${stoken}")
        ).pause(8)
	}
		
	object LoginWithoutDiagnosis {
		val loginWithoutDiagnosis = exec(
		http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
        ).pause(8)
        .exec(
            http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "amine")
			.formParam("password", "12345")
			.formParam("_csrf", "${stoken}")
        ).pause(8)
	}

	object MyPets {
		val myPets = exec(http("Mypets")
			.get("/owner/pets")
			.headers(headers_0))
		.pause(8)
	}
		
	object MyPetWithDiagnosis{
		val myPetWithDiagnosis = exec(http("SeeMyDiagnosis")
			.get("/diagnosis/myDiagnosis?ownerId=11&petId=14")
			.headers(headers_0))
		.pause(30)
	}

	object MyPetWithoutDiagnosis{
		val myPetWithoutDiagnosis = exec(http("SeeMyDiagnosis")
			.get("/diagnosis/myDiagnosis?ownerId=11&petId=19")
			.headers(headers_0))
		.pause(30)
	}


	val petWithDiagnosisScn = scenario("PetWithDiagnosis").exec(Home.home,
												LoginWithDiagnosis.loginWithDiagnosis,
												MyPets.myPets,
												MyPetWithDiagnosis.myPetWithDiagnosis)

	val petWithoutDiagnosisScn = scenario("PetWithoutDiagnosis").exec(Home.home,
												LoginWithoutDiagnosis.loginWithoutDiagnosis,
												MyPets.myPets,
												MyPetWithoutDiagnosis.myPetWithoutDiagnosis)


	setUp(
		petWithDiagnosisScn.inject(rampUsers(7000) during (100 seconds)),
		petWithoutDiagnosisScn.inject(rampUsers(7000) during (100 seconds))
		).protocols(httpProtocol)
        .assertions(
            global.responseTime.max.lt(5000),
            global.responseTime.mean.lt(1000),
            global.successfulRequests.percent.gt(95)
        ) 
}