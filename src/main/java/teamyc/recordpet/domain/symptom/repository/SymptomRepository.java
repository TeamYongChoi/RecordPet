package teamyc.recordpet.domain.symptom.repository;

import org.springframework.data.repository.RepositoryDefinition;
import teamyc.recordpet.domain.symptom.entitiy.Symptom;

@RepositoryDefinition(domainClass = Symptom.class, idClass = Long.class)
public interface SymptomRepository {

    void save(Symptom request);
}
