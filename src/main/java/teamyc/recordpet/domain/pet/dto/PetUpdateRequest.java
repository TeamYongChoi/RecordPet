package teamyc.recordpet.domain.pet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamyc.recordpet.domain.pet.entity.Gender;

@Getter
@NoArgsConstructor
public class PetUpdateRequest {

    private String name;
    private int age;
    @JsonProperty("isNeutered")
    private boolean isNeutered;
    private Gender gender;

    @Builder
    public PetUpdateRequest(String name, int age, boolean isNeutered, Gender gender) {
        this.name = name;
        this.age = age;
        this.isNeutered = isNeutered;
        this.gender = gender;
    }
}
