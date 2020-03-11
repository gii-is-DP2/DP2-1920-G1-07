<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="vets">
    <h2>Veterinarians</h2>

    <table id="vetsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Specialties</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${vets.vetList}" var="vet">
            <tr>
                <td>
                    <c:out value="${vet.firstName} ${vet.lastName}"/>
                </td>
                <td>
                    <c:forEach var="specialty" items="${vet.specialties}">
                        <c:out value="${specialty.name} "/>
                    </c:forEach>
                    <c:if test="${vet.nrOfSpecialties == 0}">none</c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <table class="table-buttons">
        <tr> 
             <td>
          <sec:authorize access="hasAnyAuthority('veterinarian')">  
            <a class="btn btn-default" href='<spring:url value="/vets/visit" htmlEscape="true"/>'>Visits List</a>
          -   </sec:authorize>  -
            </td>     
            
            <td>
            <sec:authorize access="hasAnyAuthority('admin')">
            <a class="btn btn-default" href='<spring:url value="/vets/create" htmlEscape="true"/>'>Create Vet</a>
            </sec:authorize>
            </td>  
        </tr>
    </table>
</petclinic:layout>
