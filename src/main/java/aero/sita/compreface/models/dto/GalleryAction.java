package aero.sita.compreface.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import aero.sita.compreface.models.Action;
import aero.sita.compreface.utils.MiscUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GalleryAction {

	@JsonProperty("action")
	private Action action;

	@JsonProperty("data")
	private GalleryRecord data;

	@JsonProperty("upk")
	private String upk;

	public String toJson() {
		return MiscUtil.toJson(this);
	}

}
