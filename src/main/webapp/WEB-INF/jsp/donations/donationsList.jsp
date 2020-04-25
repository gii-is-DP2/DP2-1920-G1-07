<%@page import="org.springframework.samples.petclinic.model.PayPalClient"%>
<%@page import="org.springframework.samples.petclinic.web.PaypalController"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="donations">
	<h2>Donations</h2>

	<table id="donationsTable" class="table table-striped">
		<thead>
			<tr>

				<th style="width: 200px;">Name</th>
				<th>Money</th>
				<sec:authorize access="hasAnyAuthority('admin')">
					<th>Actions</th>
				</sec:authorize>
				<th>Finish Transaction</th>
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

					<sec:authorize access="hasAnyAuthority('admin')">
						<td><spring:url
								value="/cause/{causeId}/donations/delete/{donationId}"
								var="donationUrl">
								<spring:param name="donationId" value="${donation.id}" />
								<spring:param name="causeId" value="${donation.causes.id}" />
							</spring:url> <a href="${fn:escapeXml(donationUrl)}">Delete</a></td>
					</sec:authorize>
					<td>
					<spring:url value="/paypal/make/payment" var="paypal" >
        				<spring:param name="donationId" value="${donation.id}" />
        				<spring:param name="currency" value="EUR"/>
        			</spring:url>
        			<a href="${fn:escapeXml(paypal)}">Pagar con Paypal</a>
        			</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
				<spring:url value="/cause/{causeId}/donations/new"
		var="donationUrl2">
					<spring:param name="causeId" value="${causes.id}" />
				</spring:url> 
				<a href="${fn:escapeXml(donationUrl2)}" class="btn btn-default">Add Donation</a>

  
  <spring:url value="/cause/{causeId}/donations/myDonations"
		var="myDonations">
  <spring:param name="causeId" value="${causes.id}" />
				</spring:url> 
    <a href="${fn:escapeXml(myDonations)}" class="btn btn-default">See My Donations</a>

<spring:url value="/cause" var="volver" />
	<a href="${fn:escapeXml(volver)}" class="btn btn-default">Return</a>

</petclinic:layout>>