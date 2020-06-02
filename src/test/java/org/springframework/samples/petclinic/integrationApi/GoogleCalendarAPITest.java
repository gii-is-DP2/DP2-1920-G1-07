
package org.springframework.samples.petclinic.integrationApi;

import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.model.googleCalendar.End;
import org.springframework.samples.petclinic.model.googleCalendar.Event;
import org.springframework.samples.petclinic.model.googleCalendar.Start;
import org.springframework.test.web.servlet.MockMvc;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class GoogleCalendarAPITest {

	private static final String	CLIENT_ID			= "248454269324-lk4je2a5hsmvi7lbsg3eb3shlt7lc4lh.apps.googleusercontent.com";
	private static final String	CLIENT_SECRET		= "GBHHTb3wFMiPsHHcb09tA-pX";
	private static final String	REDIRECT_URL		= "http://localhost:8080";
	private static final String	BASE_URI			= "https://localhost:8080";
	private static final String	AUTHORIZATION_CODE	= "4/zgE-hO4rAGbLSE1EGBBBJrgbM8RQgNvWk0iiJFP6vEtC_vcDaHt4FPkLLdaX35uUS2KU5RouxvGP6rIkgz0fgw";

	/**
	 * Se debe introducir un ACCESS_TOKEN VALIDO QUE PUEDE GENERAR EN: https://developers.google.com/oauthplayground/
	 * EN Select and authorized API SELECCIONAR -> CALENDAR API V3
	 * DARLE AUTORIZACION
	 * GENERAR EL TOKEN Y CREAR UNA VARIABLE DE ENTORNO QUE SE LLAME GCALENDAR_TOKEN con el access token.
	 * RECORDAR QUE EL ACCESS TOKEN EXPIRA TRAS UN PERIODO DE TIEMPO
	 */
	private static final String	ACCESS_TOKEN		= System.getenv("GCALENDAR_TOKEN");

	private MockMvc				mockMvc;
	private Map<String, String>	env;


	@Test
	public void testGetPrimaryCalendar() {
		if (!GoogleCalendarAPITest.ACCESS_TOKEN.equals(null)) {
			String calendarId = "primary";
			RestAssured.given().auth().preemptive().oauth2(GoogleCalendarAPITest.ACCESS_TOKEN).when().get("https://www.googleapis.com/calendar/v3/calendars/" + calendarId).then().statusCode(200).and().assertThat().body("kind",
				Matchers.equalTo("calendar#calendar"));
		} else {
			System.out.println("RECUERDA QUE DEBES CONFIGURAR EL ACCESS TOKEN VALIDO " + "EN TUS VARIABLES DE ENTORNO CON EL NOMBRE 'GCALENDAR_TOKEN'");
		}

	}

	@Test
	public void testInsertEventOnPrimaryCalendar() {
		if (!GoogleCalendarAPITest.ACCESS_TOKEN.equals(null)) {
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
			RestAssured.given().auth().preemptive().oauth2(GoogleCalendarAPITest.ACCESS_TOKEN).given().request().contentType(ContentType.JSON)

				.response().with().body(event).when().post("https://www.googleapis.com/calendar/v3/calendars/" + calendarId + "/events").then().statusCode(200).and().assertThat()
				//Esta son las unicas propiedades comunes que no dependen del usuario que esté introduciendo el evento
				//Y por lo tanto las unicas que podemos probar que existen.
				.body("kind", Matchers.equalTo("calendar#event")).body("status", Matchers.equalTo("confirmed")); //Es confirmed si el evento se ha insertado.
		} else {
			System.out.println("RECUERDA QUE DEBES CONFIGURAR EL ACCESS TOKEN VALIDO " + "EN TUS VARIABLES DE ENTORNO CON EL NOMBRE 'GCALENDAR_TOKEN'");
		}
	}
}
