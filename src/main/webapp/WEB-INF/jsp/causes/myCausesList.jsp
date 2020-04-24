<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="causes">
	<h2>Causes</h2>
	
	<table id="causesTable" class="table table-striped">
		<thead>
			<tr>
				<th>User</th>
				<th>Title</th>
				<th>Description</th>
				<th>Deadline</th>
				<th>Money</th>
				<th>Status</th>
			</tr>
		</thead>

		<tbody>
			<div id="mensaje">
				<c:out value="${mensaje}"></c:out>
			</div>
			<c:forEach items="${causes}" var="causes">
				<tr>
					<td>
						<c:out value="${causes.user.username}"></c:out>
					</td>
					<td>
						<c:out value="${causes.title}"></c:out>
					</td>
					<td>
						<c:out value="${causes.description}"></c:out>
					</td>
					<td>
						<c:out value="${causes.deadline}"></c:out>
					</td>
					<td>
						<c:out value="${causes.money}"></c:out>
					</td>
					<td>
						<c:out value="${causes.status.name}"></c:out>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<spring:url value="/cause" var="volver"/>
    <a href="${fn:escapeXml(volver)}" class="btn btn-default">Return</a>
</petclinic:layout>