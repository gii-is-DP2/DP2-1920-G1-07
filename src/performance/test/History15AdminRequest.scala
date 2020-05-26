package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class History15AdminRequest extends Simulation {

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

	object LoginAdmin {
   		 val loginAdmin = exec(
      			http("LoginAd")
        		.get("/login")
        		.headers(headers_0)
       		 .check(css("input[name=_csrf]", "value").saveAs("stoken"))
    		).pause(2)
    		.exec(
    		  http("LoggedAd")
        		.post("/login")
        		.headers(headers_3)
        		.formParam("username", "admin1")
       		 .formParam("password", "4dm1n")        
       		 .formParam("_csrf", "${stoken}")
    		).pause(14)
 	 }
	object Request {
		val request = exec(http("Request")
			.get("/admin/request")
			.headers(headers_0))
		.pause(10)
	}
	
	object Accept {
		val accept = exec(http("Accept")
			.get("/admin/request/1/accept")
			.headers(headers_0))
		.pause(10)
	}
	object Reject {
		val reject = exec(http("Reject")
			.get("/admin/request/2/reject")
			.headers(headers_0))
		.pause(10)
	}

	val acceptRequest = scenario("Accept").exec(Home.home, LoginAdmin.loginAdmin, Request.request, Accept.accept)
	val rejectRequest = scenario("Reject").exec(Home.home, LoginAdmin.loginAdmin, Request.request, Reject.reject)


	setUp(
		acceptRequest.inject(rampUsers(680) during (120 seconds)),
		rejectRequest.inject(rampUsers(680) during (120 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
	 )
}