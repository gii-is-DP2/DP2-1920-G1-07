<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="causes">
	<form:form modelAttribute="cause" class="form-horizontal" id="add-cause-form">
		<div class="form-group has-feedback">
			<div class="control-group">
				<petclinic:selectField name="status" label="Status" names="${status}" size="3" />
			</div>
		</div>
		
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<button class="btn btn-default" type="submit">Update Cause</button>
				<spring:url value="/cause/PendingCauses" var="volver"/>
  				<a href="${fn:escapeXml(volver)}" class="btn btn-default">Return</a>
			</div>
			
		</div>

	</form:form>

</petclinic:layout>