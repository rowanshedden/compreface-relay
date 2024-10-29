package aero.sita.compreface.models.dto;

import aero.sita.compreface.utils.MiscUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDetailsDto extends BaseRequest {

    @ApiParam(value = "flagged")
    @JsonProperty("flagged")
    @Nullable
    private Boolean flagged;

    @JsonProperty("image")
    @NotNull
    @ApiParam(value = "A picture with one face (accepted formats: jpeg, png).", required = true)
    private String image;

    @ApiParam(value = "first or given name(s) as shown on document", required = true)
    @JsonProperty("given_names")
    @NotBlank(message = "given_names cannot be empty")
    private String givenNames;

    @ApiParam(value = "last or family surname as shown on document", required = true)
    @JsonProperty("family_name")
    @NotBlank(message = "family_name cannot be empty")
    private String familyName;

    @ApiParam(value = "sex or gender M/F/X as shown on document", required = true)
    @JsonProperty("sex")
    @NotBlank(message = "sex cannot be empty")
    private String gender;

    @ApiParam(value = "Date of birth as shown on document", required = true)
    @JsonProperty("dob")
    @NotBlank(message = "dob cannot be empty")
    private String dateOfBirth;

    @ApiParam(value = "Nationality as shown on document")
    @JsonProperty("nationality")
    @Nullable
    private String nationality;

    @ApiParam(value = "Document type - P/V", required = true)
    @JsonProperty("document_type")
    @NotBlank(message = "document_type cannot be empty")
    private String documentType;

    @ApiParam(value = "Document number", required = true)
    @JsonProperty("document_number")
    @NotBlank(message = "document_number cannot be empty")
    private String documentNumber;

    @ApiParam(value = "Issuing state of document")
    @JsonProperty("issuing_state")
    @Nullable
    private String issuingState;

    @ApiParam(value = "Expiry date of document", required = true)
    @JsonProperty("expiry_date")
    @NotBlank(message = "expiry_date cannot be empty")
    private String expiryDate;

    @ApiParam(value = "Itinerary of traveller")
    @JsonProperty("itinerary")
    @Nullable
    private String itinerary;

    @ApiParam(value = "Risk assessment of traveller")
    @JsonProperty("assessment")
    @Nullable
    private String assessment;

    @ApiParam(value = "Unique passenger key")
    @JsonProperty("upk")
    @Nullable
    private String upk;

    public String getName() {
        return getGivenNames() + " " + getFamilyName();
    }

    public SubjectDetailsDto(GalleryRecord galleryRecord) {
        this.flagged = galleryRecord.isAccessAllowed();
        this.givenNames = galleryRecord.getDtc().getGivenNames();
        this.familyName = galleryRecord.getDtc().getFamilyName() != null ? galleryRecord.getDtc().getFamilyName() : "";
        this.dateOfBirth = galleryRecord.getDtc().getDateOfBirth();
        this.gender = galleryRecord.getDtc().getGender();
        this.nationality = galleryRecord.getDtc().getNationality();
        this.documentType = galleryRecord.getDtc().getDocumentType();
        this.documentNumber = galleryRecord.getDtc().getDocumentNumber();
        this.issuingState = galleryRecord.getDtc().getIssuingState();
        this.expiryDate = galleryRecord.getDtc().getExpiryDate();
        this.itinerary = MiscUtil.toJson(galleryRecord.getItinerary());
        this.assessment = MiscUtil.toJson(galleryRecord.getChecks());
        this.upk = galleryRecord.getDtc().getUpk();
        this.image = galleryRecord.getDtc().getLiveImage();
    }

}
