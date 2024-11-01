package aero.sita.compreface.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CompreFaceModel {

    @JsonProperty("id")
    public String id;

    @JsonProperty("name")
    public String name;

    @JsonProperty("apiKey")
    public String apiKey;

    @JsonProperty("type")
    public String type;

    @JsonProperty("createdDate")
    public String createdDate;

}
