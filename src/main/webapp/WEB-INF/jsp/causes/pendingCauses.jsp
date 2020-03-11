<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

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
                     	<spring:url value="/cause/PendingCauses/cause/{causeId}/edit" var="causeEditUrl">
                        	<spring:param name="causeId" value="${cause.id}"/>
                        </spring:url>
                            <a href="${fn:escapeXml(causeEditUrl)}">Edit Status</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<spring:url value="/cause" var="volver"/>
  	<a href="${fn:escapeXml(volver)}" class="btn btn-default">Return</a>

</petclinic:layout>