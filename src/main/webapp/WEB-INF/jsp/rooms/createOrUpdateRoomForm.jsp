<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="room">
	<h2>
		<c:if test="${room['new']}">New</c:if>
		Room
	</h2>
		<form:form modelAttribute="room" class="form-horizontal"
			id="add-room-form">
			<div class="form-group has-feedback">
				<petclinic:inputField label="Name" name="name" />
				<petclinic:inputField label="Capacity" name="capacity" />
				<div class="control-group">
					<petclinic:selectField name="type" label="Type " names="${types}"
						size="5" />
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<c:choose>
					<c:when test="${room['new']}">
						<button class="btn btn-default" type="submit">Add Room</button>
						<input class="btn btn-default" type="button" onclick="history.back()" name="volver atrás" value="Return">
					</c:when>
					<c:otherwise>	
						<button class="btn btn-default" type="submit">Update Room</button>
						<input class="btn btn-default" type="button" onclick="history.back()" name="volver atrás" value="Return">
					</c:otherwise>
					</c:choose>
				</div>
			</div>
		</form:form>			
</petclinic:layout>
