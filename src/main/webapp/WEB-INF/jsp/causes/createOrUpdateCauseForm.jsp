<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="causes">
	<h2>
		<c:if test="${cause['new']}">New </c:if>
		Cause
	</h2>
	<form:form modelAttribute="cause" class="form-horizontal" id="add-cause-form">
		<div class="form-group has-feedback">
			<petclinic:inputField label="Title" name="title" />
			<petclinic:inputField label="Description" name="description" />
			<petclinic:inputField label="Money" name="money" />
			<petclinic:inputField label="Deadline" name="deadline" />
			<form:errors path="*" cssClass="errorblock" style="padding-left: 211px;" element="div" />
		</div>

		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<c:choose>
					<c:when test="${cause['new']}">
						<button class="btn btn-default" type="submit">Add Cause</button>
					</c:when>
				</c:choose>
				<spring:url value="/cause" var="volver"/>
  				<a href="${fn:escapeXml(volver)}" class="btn btn-default">Return</a>
			</div>
		</div>

	</form:form>

</petclinic:layout>