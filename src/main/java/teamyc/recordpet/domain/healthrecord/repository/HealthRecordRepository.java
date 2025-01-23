package teamyc.recordpet.domain.healthrecord.repository;

import org.springframework.data.repository.RepositoryDefinition;
import teamyc.recordpet.domain.healthrecord.entitiy.HealthRecord;

@RepositoryDefinition(domainClass = HealthRecord.class, idClass = Long.class)
public interface HealthRecordRepository {

    void save(HealthRecord request);
}
