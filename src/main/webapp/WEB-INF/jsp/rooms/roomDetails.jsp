<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

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
	<c:if test="${hasPermiss==true}">
    <spring:url value="/rooms/{roomId}/edit" var="editUrl">
        <spring:param name="roomId" value="${room.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit Room</a>
	</c:if>
    <spring:url value="{roomId}/reservation/new" var="addUrl">
        <spring:param name="roomId" value="${room.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Add New Reservation</a>
    <br/>
    <br/>
    <br/>
</petclinic:layout>
