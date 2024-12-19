package teamyc.recordpet.domain.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamyc.recordpet.domain.pet.entity.Pet;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
