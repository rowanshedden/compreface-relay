package aero.sita.compreface.models.repository;

import aero.sita.compreface.models.dao.SubjectDetails;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface SubjectDetailsRepository extends PagingAndSortingRepository<SubjectDetails, UUID> {

}
