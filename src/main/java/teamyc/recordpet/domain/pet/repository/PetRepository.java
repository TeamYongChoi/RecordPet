package teamyc.recordpet.domain.pet.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.RepositoryDefinition;
import teamyc.recordpet.domain.pet.entity.Pet;

@RepositoryDefinition(domainClass = Pet.class, idClass = Long.class)
public interface PetRepository {

    Pet save(Pet pet);

    Optional<Pet> findById(Long id);

    List<Pet> findAllByUserId(Long userId);

    void deleteById(long id);

    void deleteAll();
}
