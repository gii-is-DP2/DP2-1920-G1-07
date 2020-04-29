
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
    "requestId",
    "conferenceSolutionKey",
    "status"
})
public class CreateRequest {

    @JsonProperty("requestId")
    private String requestId;
    @JsonProperty("conferenceSolutionKey")
    private ConferenceSolutionKey conferenceSolutionKey;
    @JsonProperty("status")
    private Status status;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("requestId")
    public String getRequestId() {
        return requestId;
    }

    @JsonProperty("requestId")
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @JsonProperty("conferenceSolutionKey")
    public ConferenceSolutionKey getConferenceSolutionKey() {
        return conferenceSolutionKey;
    }

    @JsonProperty("conferenceSolutionKey")
    public void setConferenceSolutionKey(ConferenceSolutionKey conferenceSolutionKey) {
        this.conferenceSolutionKey = conferenceSolutionKey;
    }

    @JsonProperty("status")
    public Status getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(Status status) {
        this.status = status;
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
