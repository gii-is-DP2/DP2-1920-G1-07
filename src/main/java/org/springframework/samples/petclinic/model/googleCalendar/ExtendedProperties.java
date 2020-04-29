
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
    "private",
    "shared"
})
public class ExtendedProperties {

    @JsonProperty("private")
    private Private _private;
    @JsonProperty("shared")
    private Shared shared;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("private")
    public Private getPrivate() {
        return _private;
    }

    @JsonProperty("private")
    public void setPrivate(Private _private) {
        this._private = _private;
    }

    @JsonProperty("shared")
    public Shared getShared() {
        return shared;
    }

    @JsonProperty("shared")
    public void setShared(Shared shared) {
        this.shared = shared;
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
