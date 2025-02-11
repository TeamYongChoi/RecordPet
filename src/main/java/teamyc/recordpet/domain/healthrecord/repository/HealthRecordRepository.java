package teamyc.recordpet.domain.healthrecord.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import teamyc.recordpet.domain.healthrecord.entitiy.HealthRecord;

import java.util.List;
import java.util.Optional;

@RepositoryDefinition(domainClass = HealthRecord.class, idClass = Long.class)
public interface HealthRecordRepository {

    void save(HealthRecord request);

    @Query("SELECT hr FROM health_record hr " +
       "JOIN FETCH hr.pet " +
       "LEFT JOIN FETCH hr.events e " +
       "WHERE hr.pet.id = :petId " +
       "AND FUNCTION('YEAR', e.occurrenceTime) = :year " +
       "AND FUNCTION('MONTH', e.occurrenceTime) = :month")
    List<HealthRecord> findByMonthAndPetId(@Param("petId") Long petId,
                                           @Param("year") int year,
                                           @Param("month") int month);

    @Query("SELECT hr FROM health_record hr where hr.pet.id = :petId")
    Page<HealthRecord> findAllByPetId(@Param("petId") Long petId,
                           Pageable pageable);

    void deleteAll();

    Optional<HealthRecord> finById(Long recordId);
}
