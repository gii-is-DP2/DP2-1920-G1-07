<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="diagnosis">
<jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#date").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    
    <jsp:body>
    <h2>
        <c:if test="${diagnosis['new']}">New </c:if> Diagnosis
    </h2>
    <form:form modelAttribute="diagnosis" class="form-horizontal" id="add-vet-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Description" name="description"/>
            <petclinic:inputField label="Date" name="date"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${diagnosis['new']}">
                        <button class="btn btn-default" type="submit">Create Diagnosis</button>
                    </c:when>
                </c:choose>
            </div>
        </div>
    </form:form>
    <spring:url value="/vets/mySpace/" var="volver" />

	<a href="${fn:escapeXml(volver)}" class="btn btn-default">Return</a>
        </jsp:body>
</petclinic:layout>