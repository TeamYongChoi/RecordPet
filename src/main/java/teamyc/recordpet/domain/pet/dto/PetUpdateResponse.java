package teamyc.recordpet.domain.pet.dto;

import lombok.Builder;
import lombok.Getter;
import teamyc.recordpet.domain.pet.entity.Gender;
import teamyc.recordpet.domain.pet.entity.Pet;

@Getter
public class PetUpdateResponse {

    private final Long id;
    private final String name;
    private final int age;
    private final Gender gender;
    private final boolean isNeutered;
    private final String photoUrl;

    @Builder
    private PetUpdateResponse(Long id, String name, int age, Gender gender, boolean isNeutered, String photoUrl) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.isNeutered = isNeutered;
        this.photoUrl = photoUrl;
    }

    public static PetUpdateResponse fromEntity(Pet pet) {
        return PetUpdateResponse.builder()
                .id(pet.getId())
                .name(pet.getName())
                .age(pet.getAge())
                .gender(pet.getGender())
                .isNeutered(pet.getIsNeutered())
                .photoUrl(pet.getProfileImageUrl())
                .build();
    }
}
