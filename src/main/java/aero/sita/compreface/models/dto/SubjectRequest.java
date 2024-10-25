package aero.sita.compreface.models.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SubjectRequest extends BaseRequest {

    @NotNull(message = "base64 image data is missing")
    @ApiModelProperty(example = "/9j...==", value = "Base 64 image text")
    private String file;
}
