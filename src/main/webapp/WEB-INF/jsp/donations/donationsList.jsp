<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="donations">
	<h2>Donations</h2>

	<table id="donationsTable" class="table table-striped">
		<thead>
			<tr>

				<th style="width: 200px;">Name</th>
				<th>Money
				<th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${donations}" var="donation">
				<tr>
					<td><c:choose>
							<c:when test="${donation.anonymous=='FALSE'}">
        	<c:out value="${donation.user.username}" />
        <br />
							</c:when>
							<c:otherwise>
        				<c:out value="Anonymous" />
        <br />
							</c:otherwise>
						</c:choose> 
						</td>
					<td><c:out value="${donation.money}" /></td>

				</tr>
			</c:forEach>
		</tbody>
	</table>
</petclinic:layout>
