package teamyc.recordpet.domain.pet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import teamyc.recordpet.domain.pet.entity.Pet;
import teamyc.recordpet.domain.pet.entity.Gender;

@Getter
@AllArgsConstructor
public class PetRegisterRequest {
    private String name;
    private int age;
    private Gender gender;
    private boolean isVaccinated;
    private String photoUrl;

    public Pet toEntity(){
        return Pet.builder()
                .name(name)
                .age(age)
                .gender(gender)
                .isVaccinated(isVaccinated)
                .photoUrl(photoUrl)
                .build();
    }
}
