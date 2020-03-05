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
				<th>Money</th>
				<th>Actions</th>

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
						</c:choose></td>
					<td><c:out value="${donation.money}" /></td>

					<td><spring:url value="/donations/delete/{donationId}"
							var="donationUrl">
							<spring:param name="donationId" value="${donation.id}" />
						</spring:url> <a href="${fn:escapeXml(donationUrl)}">Delete</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<table class="table-buttons">
		<tr>
			<td>
				<spring:url value="/donations/new"
					var="donationUrl2">
				</spring:url> 
				<a href="${fn:escapeXml(donationUrl2)}">Add Donation</a></td>
		</tr>
	</table>
</petclinic:layout>
