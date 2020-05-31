package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class History17OwnerPets extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources()
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9,en;q=0.8")
		.doNotTrackHeader("1")
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
		.pause(9)
	}

	object Login {
   		 val login = exec(
      			http("Login")
        		.get("/login")
        		.headers(headers_0)
       		 .check(css("input[name=_csrf]", "value").saveAs("stoken"))
    		).pause(2)
    		.exec(
    		  http("Logged")
        		.post("/login")
        		.headers(headers_3)
        		.formParam("username", "owner1")
       		 .formParam("password", "0wn3r")        
       		 .formParam("_csrf", "${stoken}")
    		).pause(14)
 	 }

	object Pets {
   		 val pets = exec(http("Pets")
			.get("/owner/pets")
			.headers(headers_0))
		.pause(16)
	}

	object AddPet {
   		 val addPet = exec(http("AddPetGet")
			.get("/owner/pets/new?ownerId=10")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken2"))
		).pause(9)
		.exec(http("AddPetPost")
			.post("/owner/pets/new?ownerId=10")
			.headers(headers_3)
			.formParam("id", "")
			.formParam("name", "Sebastian")
			.formParam("birthDate", "2020/05/07")
			.formParam("type", "cat")
			.formParam("_csrf", "${stoken2}"))
		.pause(11)
	}

	object EditPet {
   		 val editPet = exec(http("EditPetGet")
			.get("/owner/pets/12/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken3"))
		).pause(12)
		.exec(http("EditPetPost")
			.post("/owner/pets/12/edit")
			.headers(headers_3)
			.formParam("id", "12")
			.formParam("name", "Luckyy")
			.formParam("birthDate", "2010/06/24")
			.formParam("type", "hamster")
			.formParam("_csrf", "${stoken3}"))
		.pause(9)
	}

	val OwnerAddPet = scenario("AddPet").exec(Home.home, Login.login, Pets.pets, AddPet.addPet)
	val OwnerEditPet = scenario("EditPet").exec(Home.home, Login.login, Pets.pets, EditPet.editPet)

	setUp(
		OwnerAddPet.inject(rampUsers(550) during (120 seconds)),
		OwnerEditPet.inject(rampUsers(550) during (120 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
	)
}