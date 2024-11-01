package aero.sita.compreface.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CompreFaceApp {

	@JsonProperty("id")
	public String id;

	@JsonProperty("name")
	public String name;

	@JsonProperty("apiKey")
	public String apiKey;

	@JsonProperty("role")
	public String role;

	@JsonProperty("owner")
	public CompreFaceOwner owner;

}
