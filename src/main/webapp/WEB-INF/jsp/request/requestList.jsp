<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="Request list">

 <h2>List of request to become a sitter</h2>

    <table class="table table-striped">
        <c:forEach var="request" items="${requests}">

            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
                        <dt>User</dt>
                        <dd><c:out value="${request.user.username}"/></dd>
                        <dt>Address</dt>
                        <dd><c:out value="${request.address}"/></dd>
                        <dt>Telephone</dt>
                        <dd><c:out value="${request.telephone}"/></dd>
                        <dt>Type</dt>
                        <dd><c:out value="${request.type.name}"/></dd>
                    </dl>
                </td>
                <td valign="top">
                    <table class="table-condensed">
                                            <tr>
                            <td>
                                <spring:url value="/admin/request/{requestId}/accept" var="accept">
                                    <spring:param name="requestId" value="${request.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(accept)}">Accept request</a>
                            </td>
                            <td>
                                <spring:url value="/admin/request/{requestId}/reject" var="reject">
                                    <spring:param name="requestId" value="${request.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(reject)}">Reject request</a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>

        </c:forEach>
    </table>
</br>
</petclinic:layout>
