<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="owners">

	<h2>Event Information</h2>
	<form:form modelAttribute="event" class="form-horizontal"
		id="add-reservation-form" action="/visit/${visitId}/event/insert/" method="post">
		<div class="form-group has-feedback">

			<div class="form-group">
				<label class="col-sm-2 control-label">StartDate</label>
				<div class="col-sm-10">
					<c:out value="${event.start.dateTime}" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">EndDate</label>
				<div class="col-sm-10">
					<c:out value="${event.end.dateTime}" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">Summary</label>
				<div class="col-sm-10">
					<c:out value="${event.summary}" />
				</div>
			</div>
			<div class="col-sm-offset-2 col-sm-10">
				<button class="btn btn-default" type="submit">Add to Google Calendar</button>
				<input class="btn btn-default" type="button"
					onclick="history.back()" name="volver atrás" value="Return">
			</div>
		</div>
	</form:form>
</petclinic:layout>