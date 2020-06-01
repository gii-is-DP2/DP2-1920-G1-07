
package org.springframework.samples.petclinic.model.googleCalendar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "kind",
    "etag",
    "id",
    "status",
    "htmlLink",
    "created",
    "updated",
    "summary",
    "description",
    "location",
    "colorId",
    "creator",
    "organizer",
    "start",
    "end",
    "endTimeUnspecified",
    "recurrence",
    "recurringEventId",
    "originalStartTime",
    "transparency",
    "visibility",
    "iCalUID",
    "sequence",
    "attendees",
    "attendeesOmitted",
    "extendedProperties",
    "hangoutLink",
    "conferenceData"
})
public class Event {

    @JsonProperty("kind")
    private String kind;
    @JsonProperty("etag")
    private String etag;
    @JsonProperty("id")
    private String id;
    @JsonProperty("status")
    private String status;
    @JsonProperty("htmlLink")
    private String htmlLink;
    @JsonProperty("created")
    private String created;
    @JsonProperty("updated")
    private String updated;
    @JsonProperty("summary")
    private String summary;
    @JsonProperty("description")
    private String description;
    @JsonProperty("location")
    private String location;
    @JsonProperty("colorId")
    private String colorId;
    @JsonProperty("creator")
    private Creator creator;
    @JsonProperty("start")
    private Start start;
    @JsonProperty("end")
    private End end;
    @JsonProperty("endTimeUnspecified")
    private String endTimeUnspecified;
    @JsonProperty("recurrence")
    private List<String> recurrence = null;
    @JsonProperty("recurringEventId")
    private String recurringEventId;
    @JsonProperty("originalStartTime")
    private OriginalStartTime originalStartTime;
    @JsonProperty("transparency")
    private String transparency;
    @JsonProperty("visibility")
    private String visibility;
    @JsonProperty("iCalUID")
    private String iCalUID;
    @JsonProperty("sequence")
    private String sequence;
    @JsonProperty("attendeesOmitted")
    private String attendeesOmitted;
    @JsonProperty("hangoutLink")
    private String hangoutLink;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("kind")
    public String getKind() {
        return kind;
    }

    @JsonProperty("kind")
    public void setKind(String kind) {
        this.kind = kind;
    }

    @JsonProperty("etag")
    public String getEtag() {
        return etag;
    }

    @JsonProperty("etag")
    public void setEtag(String etag) {
        this.etag = etag;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("htmlLink")
    public String getHtmlLink() {
        return htmlLink;
    }

    @JsonProperty("htmlLink")
    public void setHtmlLink(String htmlLink) {
        this.htmlLink = htmlLink;
    }

    @JsonProperty("created")
    public String getCreated() {
        return created;
    }

    @JsonProperty("created")
    public void setCreated(String created) {
        this.created = created;
    }

    @JsonProperty("updated")
    public String getUpdated() {
        return updated;
    }

    @JsonProperty("updated")
    public void setUpdated(String updated) {
        this.updated = updated;
    }

    @JsonProperty("summary")
    public String getSummary() {
        return summary;
    }

    @JsonProperty("summary")
    public void setSummary(String summary) {
        this.summary = summary;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("location")
    public String getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(String location) {
        this.location = location;
    }

    @JsonProperty("colorId")
    public String getColorId() {
        return colorId;
    }

    @JsonProperty("colorId")
    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    @JsonProperty("creator")
    public Creator getCreator() {
        return creator;
    }

    @JsonProperty("creator")
    public void setCreator(Creator creator) {
        this.creator = creator;
    }


    @JsonProperty("start")
    public Start getStart() {
        return start;
    }

    @JsonProperty("start")
    public void setStart(Start start) {
        this.start = start;
    }

    @JsonProperty("end")
    public End getEnd() {
        return end;
    }

    @JsonProperty("end")
    public void setEnd(End end) {
        this.end = end;
    }

    @JsonProperty("endTimeUnspecified")
    public String getEndTimeUnspecified() {
        return endTimeUnspecified;
    }

    @JsonProperty("endTimeUnspecified")
    public void setEndTimeUnspecified(String endTimeUnspecified) {
        this.endTimeUnspecified = endTimeUnspecified;
    }

    @JsonProperty("recurrence")
    public List<String> getRecurrence() {
        return recurrence;
    }

    @JsonProperty("recurrence")
    public void setRecurrence(List<String> recurrence) {
        this.recurrence = recurrence;
    }

    @JsonProperty("recurringEventId")
    public String getRecurringEventId() {
        return recurringEventId;
    }

    @JsonProperty("recurringEventId")
    public void setRecurringEventId(String recurringEventId) {
        this.recurringEventId = recurringEventId;
    }

    @JsonProperty("originalStartTime")
    public OriginalStartTime getOriginalStartTime() {
        return originalStartTime;
    }

    @JsonProperty("originalStartTime")
    public void setOriginalStartTime(OriginalStartTime originalStartTime) {
        this.originalStartTime = originalStartTime;
    }

    @JsonProperty("transparency")
    public String getTransparency() {
        return transparency;
    }

    @JsonProperty("transparency")
    public void setTransparency(String transparency) {
        this.transparency = transparency;
    }

    @JsonProperty("visibility")
    public String getVisibility() {
        return visibility;
    }

    @JsonProperty("visibility")
    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    @JsonProperty("iCalUID")
    public String getICalUID() {
        return iCalUID;
    }

    @JsonProperty("iCalUID")
    public void setICalUID(String iCalUID) {
        this.iCalUID = iCalUID;
    }

    @JsonProperty("sequence")
    public String getSequence() {
        return sequence;
    }

    @JsonProperty("sequence")
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }


    @JsonProperty("attendeesOmitted")
    public String getAttendeesOmitted() {
        return attendeesOmitted;
    }

    @JsonProperty("attendeesOmitted")
    public void setAttendeesOmitted(String attendeesOmitted) {
        this.attendeesOmitted = attendeesOmitted;
    }

    @JsonProperty("hangoutLink")
    public String getHangoutLink() {
        return hangoutLink;
    }

    @JsonProperty("hangoutLink")
    public void setHangoutLink(String hangoutLink) {
        this.hangoutLink = hangoutLink;
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
