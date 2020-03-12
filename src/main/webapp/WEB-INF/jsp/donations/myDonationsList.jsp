<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="donations">
	<h2>Donations</h2>
	
	<table id="DonationsTable" class="table table-striped">
		<thead>
			<tr>
				<th>User</th>
				<th>Money</th>
			</tr>
		</thead>

		<tbody>
			<c:forEach items="${donations}" var="donations">
				<tr>
					<td>
						<c:out value="${donations.user.username}"></c:out>
					</td>
					<td>
						<c:out value="${donations.money}"></c:out>
					</td>
			
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
		<spring:url value="/cause" var="volver" />

	<a href="${fn:escapeXml(volver)}" class="btn btn-default">Return</a>
</petclinic:layout>