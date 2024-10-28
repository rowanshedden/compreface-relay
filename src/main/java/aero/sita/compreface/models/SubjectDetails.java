package aero.sita.compreface.models;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Builder
@Table(name = "subject_details", schema = "public")
public class SubjectDetails {

    @Id
    @GeneratedValue
    UUID id;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "image_id", nullable = false)
    private String imageId;

    @Column(name = "upk", nullable = false)
    private String upk;

    @Column(name = "given_names", nullable = false)
    private String givenNames;

    @Column(name = "family_name", nullable = false)
    private String familyName;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "date_of_birth", nullable = false)
    private String dateOfBirth;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "document_type", nullable = false)
    private String documentType;

    @Column(name = "document_number", nullable = false)
    private String documentNumber;

    @Column(name = "issuing_state")
    private String issuingState;

    @Column(name = "expiry_date", nullable = false)
    private String expiryDate;

    @Column(name = "itinerary")
    private String itinerary;

    @Column(name = "assessment")
    private String assessment;

}
