package teamyc.recordpet.domain.pet.dto;

import lombok.Builder;
import lombok.Getter;
import teamyc.recordpet.domain.pet.entity.Pet;

@Getter
public class GetPetProfileResponse {
    private final String name;
    private final String photoUrl;

    @Builder
    public GetPetProfileResponse(String name, String photoUrl) {
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public static GetPetProfileResponse fromEntity(Pet pet) {
        return GetPetProfileResponse.builder()
                .name(pet.getName())
                .photoUrl(pet.getProfileImageUrl())
                .build();
    }
}
