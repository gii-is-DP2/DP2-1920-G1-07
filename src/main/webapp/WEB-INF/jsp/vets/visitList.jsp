<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="visits">
  	
  	<h2>Veterinarian Information</h2>
    <table class="table table-striped">
        <tr>
            <th>Name</th>
            <td><b><c:out value="${vet.firstName} ${vet.lastName}"/></b></td>
        </tr>
        <tr>
            <th>Specialties</th>
            <td>
            <c:forEach var="specialty" items="${vet.specialties}">
            <c:out value="${specialty.name}"/>
            </c:forEach>
            <c:if test="${vet.nrOfSpecialties == 0}">none</c:if>
            </td>
        </tr>
    </table>


 	<h2>My Visits</h2>

	<table id="visitTable" class="table table-striped">
		<thead>
			<tr>
				<th>Date</th>
				<th>Description</th>
				<th>Pet</th>
				<th>Actions</th>
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
					<td>
					   <spring:url value="/vet/{vetId}/diagnosis" var="addUrl">
   						<spring:param name="vetId" value="${vet.id}"/>
   						<spring:param name="visitId" value="${visit.id}"/>
   						</spring:url>
   						<a href="${fn:escapeXml(addUrl)}" >Add diagnosis</a>
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
