<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="visits">
    <h2>Visits</h2>

	<table id="visitTable" class="table table-striped">
		<thead>
			<tr>
				<th>Date</th>
				<th>Description</th>
				<th>Pet</th>
			</tr>
		</thead>

		<tbody>
			<c:forEach items="${visits}" var="visit">
				<tr>
					<td>
						<c:out value="${visit.date}"></c:out>
					</td>
					<td>
						<c:out value="${visit.description}"></c:out>
					</td>
					<td>
						<c:out value="${visit.pet}"></c:out>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	

<!--     <table id="visitTable" class="table table-striped"> -->
<!--         <thead> -->
<!--         <tr> -->
<!--             <th>Visit</th> -->
<!--         </tr> -->
<!--         </thead> -->
<!--        <tbody> -->
<%--         <c:forEach items="${vets.visitList}" var="vet"> --%>
<!--             <tr> -->
<!--                 <td> -->
<%--                     <c:out value="${vet.firstName} ${vet.lastName}"/> --%>
<!--                 </td> -->
<!--                 <td> -->
<%--                     <c:forEach var="visit" items="${vet.visits}"> --%>
<%--                         <c:out value="${visit.id} "/> --%>
<%--                     </c:forEach> --%>
<%--                     <c:if test="${vet.nrOfVisits == 0}">none</c:if> --%>
<!--                 </td> -->
<!--             </tr> -->
<%--         </c:forEach> --%>
<!--         </tbody> -->
<!--     </table> -->
</petclinic:layout>
