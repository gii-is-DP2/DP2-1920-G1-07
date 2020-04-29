
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
    "createRequest",
    "entryPoints",
    "conferenceSolution",
    "conferenceId",
    "signature",
    "notes",
    "gadget",
    "anyoneCanAddSelf",
    "guestsCanInviteOthers",
    "guestsCanModify",
    "guestsCanSeeOtherGuests",
    "privateCopy",
    "locked",
    "reminders",
    "source",
    "attachments"
})
public class ConferenceData {

    @JsonProperty("createRequest")
    private CreateRequest createRequest;
    @JsonProperty("entryPoints")
    private List<EntryPoint> entryPoints = null;
    @JsonProperty("conferenceSolution")
    private ConferenceSolution conferenceSolution;
    @JsonProperty("conferenceId")
    private String conferenceId;
    @JsonProperty("signature")
    private String signature;
    @JsonProperty("notes")
    private String notes;
    @JsonProperty("gadget")
    private Gadget gadget;
    @JsonProperty("anyoneCanAddSelf")
    private String anyoneCanAddSelf;
    @JsonProperty("guestsCanInviteOthers")
    private String guestsCanInviteOthers;
    @JsonProperty("guestsCanModify")
    private String guestsCanModify;
    @JsonProperty("guestsCanSeeOtherGuests")
    private String guestsCanSeeOtherGuests;
    @JsonProperty("privateCopy")
    private String privateCopy;
    @JsonProperty("locked")
    private String locked;
    @JsonProperty("reminders")
    private Reminders reminders;
    @JsonProperty("source")
    private Source source;
    @JsonProperty("attachments")
    private List<Attachment> attachments = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("createRequest")
    public CreateRequest getCreateRequest() {
        return createRequest;
    }

    @JsonProperty("createRequest")
    public void setCreateRequest(CreateRequest createRequest) {
        this.createRequest = createRequest;
    }

    @JsonProperty("entryPoints")
    public List<EntryPoint> getEntryPoints() {
        return entryPoints;
    }

    @JsonProperty("entryPoints")
    public void setEntryPoints(List<EntryPoint> entryPoints) {
        this.entryPoints = entryPoints;
    }

    @JsonProperty("conferenceSolution")
    public ConferenceSolution getConferenceSolution() {
        return conferenceSolution;
    }

    @JsonProperty("conferenceSolution")
    public void setConferenceSolution(ConferenceSolution conferenceSolution) {
        this.conferenceSolution = conferenceSolution;
    }

    @JsonProperty("conferenceId")
    public String getConferenceId() {
        return conferenceId;
    }

    @JsonProperty("conferenceId")
    public void setConferenceId(String conferenceId) {
        this.conferenceId = conferenceId;
    }

    @JsonProperty("signature")
    public String getSignature() {
        return signature;
    }

    @JsonProperty("signature")
    public void setSignature(String signature) {
        this.signature = signature;
    }

    @JsonProperty("notes")
    public String getNotes() {
        return notes;
    }

    @JsonProperty("notes")
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @JsonProperty("gadget")
    public Gadget getGadget() {
        return gadget;
    }

    @JsonProperty("gadget")
    public void setGadget(Gadget gadget) {
        this.gadget = gadget;
    }

    @JsonProperty("anyoneCanAddSelf")
    public String getAnyoneCanAddSelf() {
        return anyoneCanAddSelf;
    }

    @JsonProperty("anyoneCanAddSelf")
    public void setAnyoneCanAddSelf(String anyoneCanAddSelf) {
        this.anyoneCanAddSelf = anyoneCanAddSelf;
    }

    @JsonProperty("guestsCanInviteOthers")
    public String getGuestsCanInviteOthers() {
        return guestsCanInviteOthers;
    }

    @JsonProperty("guestsCanInviteOthers")
    public void setGuestsCanInviteOthers(String guestsCanInviteOthers) {
        this.guestsCanInviteOthers = guestsCanInviteOthers;
    }

    @JsonProperty("guestsCanModify")
    public String getGuestsCanModify() {
        return guestsCanModify;
    }

    @JsonProperty("guestsCanModify")
    public void setGuestsCanModify(String guestsCanModify) {
        this.guestsCanModify = guestsCanModify;
    }

    @JsonProperty("guestsCanSeeOtherGuests")
    public String getGuestsCanSeeOtherGuests() {
        return guestsCanSeeOtherGuests;
    }

    @JsonProperty("guestsCanSeeOtherGuests")
    public void setGuestsCanSeeOtherGuests(String guestsCanSeeOtherGuests) {
        this.guestsCanSeeOtherGuests = guestsCanSeeOtherGuests;
    }

    @JsonProperty("privateCopy")
    public String getPrivateCopy() {
        return privateCopy;
    }

    @JsonProperty("privateCopy")
    public void setPrivateCopy(String privateCopy) {
        this.privateCopy = privateCopy;
    }

    @JsonProperty("locked")
    public String getLocked() {
        return locked;
    }

    @JsonProperty("locked")
    public void setLocked(String locked) {
        this.locked = locked;
    }

    @JsonProperty("reminders")
    public Reminders getReminders() {
        return reminders;
    }

    @JsonProperty("reminders")
    public void setReminders(Reminders reminders) {
        this.reminders = reminders;
    }

    @JsonProperty("source")
    public Source getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(Source source) {
        this.source = source;
    }

    @JsonProperty("attachments")
    public List<Attachment> getAttachments() {
        return attachments;
    }

    @JsonProperty("attachments")
    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
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
