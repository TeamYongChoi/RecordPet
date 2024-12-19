package teamyc.recordpet.domain.pet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import teamyc.recordpet.domain.pet.entity.Gender;
import teamyc.recordpet.domain.pet.entity.Pet;

@Getter
public class PetResponse {

    private final Long id;
    private final String name;
    private final int age;
    private final Gender gender;

    @JsonProperty("isVaccinated")
    private final boolean isVaccinated;
    private final String photoUrl;

    @Builder
    private PetResponse(Long id, String name, int age, Gender gender, boolean isVaccinated, String photoUrl) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.isVaccinated = isVaccinated;
        this.photoUrl = photoUrl;
    }

    public static PetResponse fromEntity(Pet pet) {
        return PetResponse.builder()
                .id(pet.getId())
                .name(pet.getName())
                .age(pet.getAge())
                .gender(pet.getGender())
                .isVaccinated(pet.getIsVaccinated())
                .photoUrl(pet.getPhotoUrl())
                .build();
    }
}
