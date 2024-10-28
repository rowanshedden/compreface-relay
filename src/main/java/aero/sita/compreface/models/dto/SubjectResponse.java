package aero.sita.compreface.models.dto;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SubjectResponse extends BaseResponse {

    @ApiModelProperty(example = "640e07c4-856b-4d85-9867-7c1fe24b773e", value = "Unique image identifier")
    private String image_id;

    @ApiModelProperty(example = "Rowan Shedden", value = "Subject name")
    private String subject;

    @ApiModelProperty(example = "6", value = "Deleted record number")
    private String deleted;
}
