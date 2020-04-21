<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="changeSitter">
	<h2>Change sitter for room: ${room.name}</h2>
	<c:if test="${room.sitter == null}">
	<h5>Actual sitter: None</h5>
	</c:if>
	<c:if test="${room.sitter != null}">
	<h5>Actual sitter: ${room.sitter.user.username}</h5>
	</c:if>
	
	<b>Select new sitter</b>
	<form:form method="POST" action="/rooms/${room.id}/sitter" class="form-horizontal"
			id="change-sitter-form" modelAttribute="room">
			<div class="form-group has-feedback">
				<div class="control-group">
					<petclinic:selectField name="sitter" label="Sitters: " names="${sitters}"
						size="5" />
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
						<button class="btn btn-default" type="submit">Change sitter</button>
						<input class="btn btn-default" type="button" onclick="history.back()" name="volver atrás" value="Return">
				</div>
			</div>
		</form:form>			
</petclinic:layout>