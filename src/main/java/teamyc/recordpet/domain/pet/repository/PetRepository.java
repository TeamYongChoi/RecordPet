package teamyc.recordpet.domain.pet.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import software.amazon.awssdk.profiles.Profile;
import teamyc.recordpet.domain.pet.entity.Pet;

import java.util.List;
import java.util.Optional;

@RepositoryDefinition(domainClass = Pet.class, idClass = Long.class)
public interface PetRepository {

    Pet save(Pet pet);

    Optional<Pet> findById(Long id);

    Optional<List<Pet>> findAllByUserId(Long userId);

    void deleteById(long id);

    void deleteAll();
}
