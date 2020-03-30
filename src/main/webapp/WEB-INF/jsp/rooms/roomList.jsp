<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<petclinic:layout pageName="rooms">
    <h2>Rooms</h2>

    <table id="roomsTable" class="table table-striped">
        <thead>
        <tr>
        	<th>Name</th>
            <th>Capacity</th>
            <th>PetType</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${rooms}" var="room">
            <tr>
            	<td>
             	<spring:url value="/rooms/{roomId}" var="roomUrl">
                        <spring:param name="roomId" value="${room.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(roomUrl)}"><c:out value="${room.name}"/></a>  
                </td>
                <td>
                    <c:out value="${room.capacity}"/>
                </td>
                <td>
                        <c:out value="${room.type} "/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <table class="table-buttons">
        <tr>
       	<sec:authorize access="hasAnyAuthority('admin')">
        <td>
        	<a href="/rooms/new" class="btn btn-default">Add New Room</a>
		</td> 
		</sec:authorize>
<!--             <td> -->
<%--                 <a href="<spring:url value="/vets.xml" htmlEscape="true" />">View as XML</a> --%>
<!--             </td>             -->
        </tr>
    </table>
</petclinic:layout>
