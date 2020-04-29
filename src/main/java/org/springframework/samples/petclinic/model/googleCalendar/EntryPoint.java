
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
    "entryPointType",
    "uri",
    "label",
    "pin",
    "accessCode",
    "meetingCode",
    "passcode",
    "password"
})
public class EntryPoint {

    @JsonProperty("entryPointType")
    private String entryPointType;
    @JsonProperty("uri")
    private String uri;
    @JsonProperty("label")
    private String label;
    @JsonProperty("pin")
    private String pin;
    @JsonProperty("accessCode")
    private String accessCode;
    @JsonProperty("meetingCode")
    private String meetingCode;
    @JsonProperty("passcode")
    private String passcode;
    @JsonProperty("password")
    private String password;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("entryPointType")
    public String getEntryPointType() {
        return entryPointType;
    }

    @JsonProperty("entryPointType")
    public void setEntryPointType(String entryPointType) {
        this.entryPointType = entryPointType;
    }

    @JsonProperty("uri")
    public String getUri() {
        return uri;
    }

    @JsonProperty("uri")
    public void setUri(String uri) {
        this.uri = uri;
    }

    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    @JsonProperty("label")
    public void setLabel(String label) {
        this.label = label;
    }

    @JsonProperty("pin")
    public String getPin() {
        return pin;
    }

    @JsonProperty("pin")
    public void setPin(String pin) {
        this.pin = pin;
    }

    @JsonProperty("accessCode")
    public String getAccessCode() {
        return accessCode;
    }

    @JsonProperty("accessCode")
    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    @JsonProperty("meetingCode")
    public String getMeetingCode() {
        return meetingCode;
    }

    @JsonProperty("meetingCode")
    public void setMeetingCode(String meetingCode) {
        this.meetingCode = meetingCode;
    }

    @JsonProperty("passcode")
    public String getPasscode() {
        return passcode;
    }

    @JsonProperty("passcode")
    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
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
