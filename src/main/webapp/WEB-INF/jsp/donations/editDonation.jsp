<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="donations">
  
    <jsp:body>
        <h2>Donation</h2>

        <form:form modelAttribute="donation" class="form-horizontal" action="/cause/donations/save">
            	 <div class="form-group has-feedback">
               <div class="form-group">
                    <label class="col-sm-2 control-label">Cause</label>
                    <div class="col-sm-10">
                        <c:out value="${donation.causes.title}" />
                    </div>
                </div>
                <petclinic:inputField label="Money" name="money"/>
                <petclinic:inputField label="Anonymous" name="anonymous"/>
                
                
                
                
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="id" value="${donation.id}"/>
                    <button class="btn btn-default" type="submit">Save Donation</button>
                </div>
            </div>
        </form:form>

       
    </jsp:body>

</petclinic:layout>