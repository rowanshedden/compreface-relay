package aero.sita.compreface.models.repository;

import aero.sita.compreface.models.dao.CompreFaceStore;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface RelayRepository extends PagingAndSortingRepository<CompreFaceStore, UUID> {

}
