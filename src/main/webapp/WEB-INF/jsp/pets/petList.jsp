<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="myPets">
 
  <div class="alert alert-warning" role="alert">
  		Remember. If you want to add visits to Google Calendar you must log in with Google!
  </div>
  <div class="row">
  
  <spring:url value="/singIn/google" var="eventUrl"/>
  <a type="button" class="btn btn-success" style="margin-left: 30pc;" href="${fn:escapeXml(eventUrl)}">Log in with Google</a>
  </div>
 
 <h2>Pets and Visits</h2>

    <table class="table table-striped">
        <c:forEach var="pet" items="${owner.pets}">

            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
                        <dt>Name</dt>
                        <dd><c:out value="${pet.name}"/></dd>
                        <dt>Birth Date</dt>
                        <dd><petclinic:localDate date="${pet.birthDate}" pattern="yyyy-MM-dd"/></dd>
                        <dt>Type</dt>
                        <dd><c:out value="${pet.type.name}"/></dd>
                    </dl>
                </td>
                <td valign="top">
                    <table class="table-condensed">
                        <thead>
                        <tr>
                            <th>Visit Date</th>
                            <th>Description</th>
                            <th>Google Calendar</th>
                        </tr>
                        </thead>
                        <c:forEach var="visit" items="${pet.visits}">
                            <tr>
                                <td><petclinic:localDate date="${visit.date}" pattern="yyyy-MM-dd"/></td>
                                <td><c:out value="${visit.description}"/></td>
                            	<td>
                            	<c:if test="${haceAccessToken == true }">
                            	<spring:url value="/visit/{visitId}/event/insert/" var="eventUrl">
                            		<spring:param name="visitId" value="${visit.id}"/>
                            	</spring:url>
                            	
                            	<a href="${fn:escapeXml(eventUrl)}">Add</a>
                            	</c:if>
                            	</td>
              					</tr>
                        </c:forEach>
                        <tr>
                            <td>
                                <spring:url value="/owner/pets/{petId}/edit" var="petUrl">
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(petUrl)}">Edit Pet</a>
                            </td>
                            <td>
                                <spring:url value="/owner/pets/{petId}/delete" var="deleteUrl">
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(deleteUrl)}">Delete Pet</a>
                            </td>
                            <td>
                                <spring:url value="/vets" var="visitUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(visitUrl)}">Add Visit</a>
                            </td>
                            
                             <td>
                                <spring:url value="/diagnosis/myDiagnosis" var="diagnosistUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(diagnosistUrl)}">See my Diagnosis</a>
                            </td>
                                                    
                        </tr>
                    </table>
                </td>
            </tr>

        </c:forEach>
    </table>
</br>
  <spring:url value="pets/new" var="addUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Add New Pet</a>
	
</petclinic:layout>

