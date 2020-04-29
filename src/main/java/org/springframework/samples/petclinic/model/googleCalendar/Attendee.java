
package org.springframework.samples.petclinic.model.googleCalendar;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "email",
    "displayName",
    "organizer",
    "self",
    "resource",
    "optional",
    "responseStatus",
    "comment",
    "additionalGuests"
})
public class Attendee {

    @JsonProperty("id")
    private String id;
    @JsonProperty("email")
    private String email;
    @JsonProperty("displayName")
    private String displayName;
    @JsonProperty("organizer")
    private String organizer;
    @JsonProperty("self")
    private String self;
    @JsonProperty("resource")
    private String resource;
    @JsonProperty("optional")
    private String optional;
    @JsonProperty("responseStatus")
    private String responseStatus;
    @JsonProperty("comment")
    private String comment;
    @JsonProperty("additionalGuests")
    private String additionalGuests;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("displayName")
    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty("displayName")
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @JsonProperty("organizer")
    public String getOrganizer() {
        return organizer;
    }

    @JsonProperty("organizer")
    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    @JsonProperty("self")
    public String getSelf() {
        return self;
    }

    @JsonProperty("self")
    public void setSelf(String self) {
        this.self = self;
    }

    @JsonProperty("resource")
    public String getResource() {
        return resource;
    }

    @JsonProperty("resource")
    public void setResource(String resource) {
        this.resource = resource;
    }

    @JsonProperty("optional")
    public String getOptional() {
        return optional;
    }

    @JsonProperty("optional")
    public void setOptional(String optional) {
        this.optional = optional;
    }

    @JsonProperty("responseStatus")
    public String getResponseStatus() {
        return responseStatus;
    }

    @JsonProperty("responseStatus")
    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    @JsonProperty("comment")
    public String getComment() {
        return comment;
    }

    @JsonProperty("comment")
    public void setComment(String comment) {
        this.comment = comment;
    }

    @JsonProperty("additionalGuests")
    public String getAdditionalGuests() {
        return additionalGuests;
    }

    @JsonProperty("additionalGuests")
    public void setAdditionalGuests(String additionalGuests) {
        this.additionalGuests = additionalGuests;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
