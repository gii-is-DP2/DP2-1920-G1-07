<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<petclinic:layout pageName="rooms">

    <h2>Room Information</h2>


    <table class="table table-striped">
        <tr>
            <th>Name</th>
            <td><b><c:out value="${room.name}"/></b></td>
        </tr>
        <tr>
            <th>Capacity</th>
            <td><c:out value="${room.capacity}"/></td>
        </tr>
        <tr>
            <th>Pet Type</th>
            <td><c:out value="${room.type}"/></td>
        </tr>
    </table>
	<sec:authorize access="hasAnyAuthority('admin')">
    	<c:if test="${notHaveReservations==true}">
    		<spring:url value="/rooms/{roomId}/edit" var="editUrl">
        		<spring:param name="roomId" value="${room.id}"/>
    		</spring:url>
    		<a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit Room</a>
		<td>
   			<spring:url value="/rooms/delete/{roomId}" var="deleteUrl">
      			<spring:param name="roomId" value="${room.id}"/>
   			</spring:url>
   			<a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Delete Room</a>
        </td>
        </c:if>
      </sec:authorize>
		<c:if test="${completedRoom == false}">
    		<spring:url value="/rooms/{roomId}/reservations/new" var="addUrl">
        		<spring:param name="roomId" value="${room.id}"/>
    		</spring:url>
    		<a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Add New Reservation</a>
   		</c:if>
   		<c:if test="${completedRoom == true}">
   			<div class="alert alert-info">
   				<br/>
   				<h3>ESTA HABITACION TIENE LAS RESERVAS AGOTADAS</h3>
   			</div>
   		</c:if>
    <br/>
    <br/>
    <br/>
    <h2>Reservations of these Room</h2>
     <table class="table table-striped">
     <thead>
        <tr>
            <th style="width: 150px;">Owner name</th> 
            <th style="width: 200px;">Room name</th> 
            <th>Entry Date</th>
            <th>Exit Date</th>
            <th>Pet Name</th>
            <th>Status</th>
        </tr>
        </thead>
        <tbody>
        <sec:authorize access="hasAnyAuthority('admin')">
        <c:forEach var="reservation" items="${room.reservations}">
        <tr>
                <td>
                    <c:out value="${reservation.owner.firstName}, ${reservation.owner.lastName}" />
	            </td> 
                 <td> 
                     <c:out value="${reservation.room.name}"/>
                 </td> 
                <td>
                    <petclinic:localDate date="${reservation.entryDate}" pattern="yyyy/MM/dd"/>
                </td>
                <td>
                	<petclinic:localDate date="${reservation.exitDate}" pattern="yyyy/MM/dd"/>
                </td>
                
                <td> 
                    <c:out value="${reservation.pet}"/> 
                </td>
                 <sec:authorize access="hasAnyAuthority('admin')">
                <td>
                <spring:url value="/rooms/{roomId}/{ownerId}/reservation/{reservationId}/edit" var="editUrl">
                		<spring:param name="roomId" value="${reservation.room.id}"/>
                		<spring:param name="reservationId" value="${reservation.id}"/>  
                		<spring:param name="ownerId" value="${reservation.owner.id}"/>    
                </spring:url>
                <a href="${fn:escapeXml(editUrl)}"><c:out value="${reservation.status}"/></a>
                </td>
                </sec:authorize>
                
            </tr>
        </c:forEach>
        </sec:authorize>
        <sec:authorize access="hasAnyAuthority('owner')">
        <c:forEach var="reservation" items="${myReservations}">
        <tr>
                <td>
                    <c:out value="${reservation.owner.firstName}, ${reservation.owner.lastName}" />
	            </td> 
                 <td> 
                     <c:out value="${reservation.room.name}"/>
                 </td> 
                <td>
                    <petclinic:localDate date="${reservation.entryDate}" pattern="yyyy/MM/dd"/>
                </td>
                <td>
                	<petclinic:localDate date="${reservation.exitDate}" pattern="yyyy/MM/dd"/>
                </td>
                
                <td> 
                    <c:out value="${reservation.pet}"/> 
                </td>
                <sec:authorize access="hasAnyAuthority('admin')">
                <td>
                <spring:url value="/rooms/{roomId}/{ownerId}/reservation/{reservationId}/edit" var="editUrl">
                		<spring:param name="roomId" value="${reservation.room.id}"/>
                		<spring:param name="reservationId" value="${reservation.id}"/>  
                		<spring:param name="ownerId" value="${reservation.owner.id}"/>    
                </spring:url>
                <a href="${fn:escapeXml(editUrl)}"><c:out value="${reservation.status}"/></a>
                </td>
                </sec:authorize>
                <sec:authorize access="hasAnyAuthority('owner')">
                	<td>
                		<c:out value="${reservation.status}"/>
                	</td>
                </sec:authorize>
            </tr>
        </c:forEach>
        </sec:authorize>
 		
        </tbody>
     </table>

</petclinic:layout>
