<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

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
		
		 <h2>Edit Reservation</h2>
       <form:form modelAttribute="editreservation" class="form-horizontal"
				id="add-editreservation-form">
        <div class="form-group has-feedback">
             <petclinic:inputField label="Entry Date" name="entryDate" /> 
             <petclinic:inputField label="Exit Date" name="exitDate"/> 
             <div class="form-group"> 
                     <label class="col-sm-2 control-label">Owner</label> 
                     <div class="col-sm-10">
                         <input type="text" readonly="readonly"
								value="<c:out value="${editreservation.owner.firstName} ${editreservation.owner.lastName}"/>" /> 
                     </div> 
                 </div> 
          <div class="form-group"> 
                 <div class="control-group">  	
                        <c:choose>
                        <c:when test="${completedRoom == false }">
                        <petclinic:selectField2 label="Status"
										name="status" names="${status}" size="3" itemLabel="name" />
                        </c:when>
                        <c:otherwise>
                        <petclinic:selectField2 label="Status"
										name="status" names="${statusWithouthAccepted}" size="2"
										itemLabel="name" />
                     	</c:otherwise>
                     	</c:choose>
                     </div>
             	<div class="control-group"> 
                         <petclinic:selectField2 label="Pet Name"
								name="pet" names="${editreservation.owner.pets}" size="1"
								itemLabel="name" /> 
                
                </div> 
         </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="roomId"
								value="${editreservation.room.id}" />
	                <input type="hidden" name="ownerId"
								value="${editreservation.owner.id}" />
                    <input type="hidden" name="petId"
								value="${editreservation.pet}" /> 
             <div class="form-group">
            
            <c:if test="${completedRoom==true}">
            <div class="alert alert-info">
   				<br />
   				<h3>ESTA HABITACION TIENE LAS RESERVAS AGOTADAS</h3>
   			</div>
			<a class="btn btn-default" type="button" href="/rooms/${editreservation.room.id}">Return</a>            
			</c:if>
			 <div class="col-sm-offset-0 col-sm-10">
              <button class="btn btn-default" type="submit">Update Reservation</button>
            </div>
            </div>
                </div>
            </div>
            </div>
		</form:form>
        <br/>
    </jsp:body>
	</petclinic:layout>