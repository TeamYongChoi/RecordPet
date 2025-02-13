package teamyc.recordpet.domain.pet.repository;

import org.springframework.data.repository.RepositoryDefinition;
import teamyc.recordpet.domain.pet.entity.Pet;

import java.util.List;
import java.util.Optional;

@RepositoryDefinition(domainClass = Pet.class, idClass = Long.class)
public interface PetRepository {

    Pet save(Pet pet);

    Optional<Pet> findById(Long id);

    List<Pet> findAll();

    void deleteById(long id);

    void deleteAll();

    boolean existsById(Long id);
}
