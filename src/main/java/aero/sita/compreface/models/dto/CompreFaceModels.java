package aero.sita.compreface.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CompreFaceModels {

    @JsonProperty("models")
    public List<CompreFaceModel> models = new ArrayList<CompreFaceModel>();

}
