<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="rooms">
    <h2>Rooms</h2>

    <table id="roomsTable" class="table table-striped">
        <thead>
        <tr>
        	<th>Name</th>
            <th>Capacity</th>
            <th>Sitter</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${rooms}" var="room">
            <tr>
                <td>
                	<c:out value="${room.name}"/>
                </td>
                <td>
                    <c:out value="${room.capacity}"/>
                </td>
                <td>
                        <c:out value="${room.type} "/>
                    <c:if test="${room.type == null}">none</c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <table class="table-buttons">
        <tr>
<!--             <td> -->
<%--                 <a href="<spring:url value="/vets.xml" htmlEscape="true" />">View as XML</a> --%>
<!--             </td>             -->
        </tr>
    </table>
</petclinic:layout>
