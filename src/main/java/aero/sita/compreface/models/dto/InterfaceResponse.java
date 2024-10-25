package aero.sita.compreface.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class InterfaceResponse extends BaseResponse {

    @JsonProperty("active-interface")
    private String activeinterface;

}
