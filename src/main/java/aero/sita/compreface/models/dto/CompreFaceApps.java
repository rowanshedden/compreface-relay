package aero.sita.compreface.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CompreFaceApps {

	@JsonProperty("apps")
	public List<CompreFaceApp> apps = new ArrayList<CompreFaceApp>();

}
