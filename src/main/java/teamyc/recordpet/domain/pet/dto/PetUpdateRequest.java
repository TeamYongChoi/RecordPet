package teamyc.recordpet.domain.pet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor
public class PetUpdateRequest {
    private String name;
    private int age;
    @JsonProperty("isVaccinated")
    private boolean isVaccinated;
    private String photoUrl;

    @Builder
    public PetUpdateRequest(String name, int age, boolean isVaccinated, String photoUrl) {
        this.name = name;
        this.age = age;
        this.isVaccinated = isVaccinated;
        this.photoUrl = photoUrl;
    }

}
