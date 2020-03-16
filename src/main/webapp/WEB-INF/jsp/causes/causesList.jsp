<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="cause">
	<h2>Causes</h2>
	
	<table id="causesTable" class="table table-striped">
		<thead>
			<tr>
				<th>Title</th>
				<th>Description</th>
				<th>Deadline</th>
				<th>Money</th>
			</tr>
		</thead>

		<tbody>
			<c:forEach items="${cause}" var="cause">
				<tr>
					<td>
						<c:out value="${cause.title}"></c:out>
					</td>
					<td>
						<c:out value="${cause.description}"></c:out>
					</td>
					<td>
						<c:out value="${cause.deadline}"></c:out>
					</td>
					<td>
						<c:out value="${cause.money}"></c:out>
					</td>
					<td>
					   <spring:url value="/cause/{causeId}/donations" var="addUrl">
   						<spring:param name="causeId" value="${cause.id}"/>
   						</spring:url>
   						<a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Add donation</a>
   						</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
    <spring:url value="/cause/new" var="addCause"/>
    <a href="${fn:escapeXml(addCause)}" class="btn btn-default">Add New Cause</a>
    
    <spring:url value="/cause/myCauses" var="myCauses"/>
    <a href="${fn:escapeXml(myCauses)}" class="btn btn-default">See My Causes</a>
    
   <sec:authorize access="hasAnyAuthority('admin')"> 
    <spring:url value="/cause/PendingCauses" var="pending"/>
    <a href="${fn:escapeXml(pending)}" class="btn btn-default">See Pending Causes</a>
   </sec:authorize>
	
</petclinic:layout>