package org.springframework.samples.petclinic.integrationApi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.Before;
import org.eclipse.jdt.internal.compiler.batch.Main;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.restlet.Request;
import org.springframework.samples.petclinic.model.googleCalendar.End;
import org.springframework.samples.petclinic.model.googleCalendar.Event;
import org.springframework.samples.petclinic.model.googleCalendar.Start;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.google.api.client.util.DateTime;

import io.restassured.response.*;
import lombok.Builder.ObtainVia;
import io.restassured.path.json.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import javax.servlet.http.HttpServletRequest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.concurrent.TimeUnit;


public class GoogleCalendarAPITest {
	
	private static final String CLIENT_ID = "248454269324-lk4je2a5hsmvi7lbsg3eb3shlt7lc4lh.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "GBHHTb3wFMiPsHHcb09tA-pX";
	private static final String REDIRECT_URL = "http://localhost:8080";
	private static final String BASE_URI = "https://localhost:8080";
	private static final String AUTHORIZATION_CODE = "4/zgE-hO4rAGbLSE1EGBBBJrgbM8RQgNvWk0iiJFP6vEtC_vcDaHt4FPkLLdaX35uUS2KU5RouxvGP6rIkgz0fgw";
	
	/** 
	 * Se debe introducir un ACCESS_TOKEN VALIDO QUE PUEDE GENERAR EN: https://developers.google.com/oauthplayground/
	 * EN Select and authorized API SELECCIONAR -> CALENDAR API V3
	 * DARLE AUTORIZACION 
	 * GENERAR EL TOKEN Y SUSTITUIR EN LA SIGUIENTE VARIABLE ESTÁTICA
	 */
	private static final String ACCESS_TOKEN = System.getenv("TOKEN_VALIDO_GCALENDAR");
	
	private MockMvc mockMvc;
	
	
	@Test
	public void testGetPrimaryCalendar() {
		String calendarId = "primary";
		RestAssured.given().auth().preemptive().oauth2(ACCESS_TOKEN)
		.when()
			.get("https://www.googleapis.com/calendar/v3/calendars/"+calendarId)
		.then()
			.statusCode(200)
		.and()
			.assertThat()
				.body("kind", equalTo("calendar#calendar"));
	}
	
	@Test
	public void testInsertEventOnPrimaryCalendar() { 
		Start start = new Start();
		String dateTimeStart = "2020-09-20T10:00:00";
		start.setDateTime(dateTimeStart);
		start.setTimeZone("UTC");
		End end = new End();
		end.setDateTime("2020-09-20T11:00:00");
		end.setTimeZone("UTC");
		Event event = new Event();
		event.setStart(start);
		event.setEnd(end);
		event.setSummary("Ejemplo de Evento creado en el Test del Pet Clinic del grupo G7 de DP2-BORRAR-");
		String calendarId = "primary";
		RestAssured.given().auth().preemptive().oauth2(ACCESS_TOKEN)
			.given()
				.request().contentType(ContentType.JSON)
						  .log().all()
				.response().log().all()
			.with()
				.body(event)
			.when()
				.post("https://www.googleapis.com/calendar/v3/calendars/"+calendarId+"/events")
			.then()
				.statusCode(200)
			.and()
				.assertThat()
					//Esta son las unicas propiedades comunes que no dependen del usuario que esté introduciendo el evento
					//Y por lo tanto las unicas que podemos probar que existen.
					.body("kind", equalTo("calendar#event"))
					.body("status", equalTo("confirmed")); //Es confirmed si el evento se ha insertado.
	}
}
