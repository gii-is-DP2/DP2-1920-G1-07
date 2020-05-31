<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<c:if test="${reservation['new']}">
<petclinic:layout pageName="reservations">
	<jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#entryDate").datepicker({dateFormat: 'yy/mm/dd'});
            });
            $(function () {
                $("#exitDate").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
	<jsp:body>
        
        <h2>New Reservation</h2>

        <b>Reservation</b>
       <form:form modelAttribute="reservation" class="form-horizontal"
				id="add-reservation-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Entry Date" name="entryDate" />
            <petclinic:inputField label="Exit Date" name="exitDate" />
             <div class="form-group"> 
                     <label class="col-sm-2 control-label">Owner</label> 
                     <div class="col-sm-10">
                         <c:out
								value="${owner.firstName} ${owner.lastName}" /> 
                     </div> 
                 </div> 
                 <div class="form-group"> 
                     <label class="col-sm-2 control-label">Status</label> 
                     	<div class="col-sm-10">
                         		<c:out value="${statusPending}" /> 
                        </div>
                 </div>
             	<div class="control-group"> 
             		<petclinic:selectField2 label="Pet Name" name="pet"
							names="${owner.pets}" size="5" itemLabel="name" />
        		</div>
         </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    
	                <input type="hidden" name="ownerId"
							value="${reservation.owner.id}" />
                    <input type="hidden" name="petId"
							value="${reservation.pet}" /> 
             <div class="form-group">
             <div class="col-sm-offset-2 col-sm-10">
                   <button class="btn btn-default" type="submit">Add Reservation</button>
                   <input class="btn btn-default" type="button" onclick="window.location.href='/rooms/${roomId}'" name="volver atrás" value="Return">
            </div>
        </div>
                </div>
            </div>
		</form:form>
		
		</jsp:body>
</petclinic:layout>
</c:if>
		<c:if test="${!reservation['new'] and exucuteThis == true}">

	</c:if>
