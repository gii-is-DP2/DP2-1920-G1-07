<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="rooms">
    <h2>Rooms</h2>

    <table id="roomsTable" class="table table-striped">
        <thead>
        <tr>
        	<th>Name</th>
            <th>Capacity</th>
            <th>PetType</th>
            <c:if test="${hasPermiss==true}">
            <th>Delete</th>
            </c:if>
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
                    <c:if test="${room.type == null}">none</c:if>
                </td>
            	<c:if test="${hasPermiss==true}">
            	<td>
            		<spring:url value="/rooms/delete/{roomId}" var="deleteUrl">
            			<spring:param name="roomId" value="${room.id}"/>
            		</spring:url>
            		<a href="${fn:escapeXml(deleteUrl)}">Delete</a>
            	</td>
            	</c:if>
            </tr>
        </c:forEach>
       
       
      
        </tbody>
    </table>

    <table class="table-buttons">
        <tr>
       	<c:if test="${hasPermiss == true}">
        <td>
        	<a href="/rooms/new" class="btn btn-default">Add New Room</a>
		</td> 
		</c:if>
<!--             <td> -->
<%--                 <a href="<spring:url value="/vets.xml" htmlEscape="true" />">View as XML</a> --%>
<!--             </td>             -->
        </tr>
    </table>
</petclinic:layout>
