<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="diagnosis">
	<h2>My Diagnosis</h2>
	
	<table id="diagnosisTable" class="table table-striped">
		<thead>
			<tr>
				<th>Description</th>
				<th>Data</th>
			</tr>
		</thead>

		<tbody>
			<c:forEach items="${diagnosis}" var="diagnosis">
				<tr>
					<td>
						<c:out value="${diagnosis.description}"></c:out>
					</td>
					<td>
						<c:out value="${diagnosis.date}"></c:out>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<spring:url value="/owner/pets" var="volver"/>
    <a href="${fn:escapeXml(volver)}" class="btn btn-default">Return</a>
</petclinic:layout>