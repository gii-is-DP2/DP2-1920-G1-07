<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<petclinic:layout pageName="donations">

	<jsp:body>
        <h2>Donation</h2>

        <form:form modelAttribute="donation" class="form-horizontal">
        	<div class="form-group has-feedback">
            	<div class="form-group">
                	<label class="col-sm-2 control-label">Cause</label>
                    	<div class="col-sm-10">
                        	<c:out value="${donation.causes.title}" />
                    	</div>
                </div>
				<div class="form-group has-feedback">
               		<div class="form-group">
                    	<label class="col-sm-2 control-label">User</label>
                    		<div class="col-sm-10">
                      			<c:out value="${donation.user.username}" />
                    		</div>
                	</div>
                	<div class="form-group has-feedback">
               		<div class="form-group">
                    	<label class="col-sm-2 control-label">Remaining money</label>
                    		<div class="col-sm-10">
                      			<c:out value="${donation.moneyRest}" />
                    		</div>
                	</div>
                <petclinic:inputField label="Money" name="money" />
				<petclinic:selectField label="¿Anonymous?" name="anonymous" size="2"
							names="${anonymous}" />
                
                </div>
            </div>
            

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="id"
							value="${donation.id}" />
                    <button class="btn btn-default" type="submit">Save Donation</button>
                    <spring:url value="/cause" var="volver" />
	
					<a href="${fn:escapeXml(volver)}" class="btn btn-default">Return</a>
                    
                </div>
            </div>
        
		</form:form>

       
    </jsp:body>

</petclinic:layout>