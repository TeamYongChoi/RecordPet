package teamyc.recordpet.domain.pet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import teamyc.recordpet.domain.pet.entity.Gender;
import teamyc.recordpet.domain.pet.entity.Pet;

@Getter
public class GetPetDetailProfileResponse {

    private final String name;
    private final int age;
    private final Gender gender;

    @JsonProperty("isNeutered")
    private final boolean isNeutered;
    private final String photoUrl;

    @Builder
    public GetPetDetailProfileResponse(String name, int age, Gender gender, boolean isNeutered,
        String photoUrl) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.isNeutered = isNeutered;
        this.photoUrl = photoUrl;
    }

    public static GetPetDetailProfileResponse fromEntity(Pet pet) {
        String profileImageUrl = pet.getProfileImageUrl();

        return GetPetDetailProfileResponse.builder()
            .name(pet.getName())
            .age(pet.getAge())
            .gender(pet.getGender())
            .isNeutered(pet.getIsNeutered())
            .photoUrl(profileImageUrl)
            .build();
    }

    @JsonProperty("isNeutered")
    public boolean isNeutered() {
        return isNeutered;
    }
}
