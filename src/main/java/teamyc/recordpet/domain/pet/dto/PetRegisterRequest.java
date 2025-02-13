package teamyc.recordpet.domain.pet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import teamyc.recordpet.domain.pet.entity.Gender;
import teamyc.recordpet.domain.pet.entity.Pet;
import teamyc.recordpet.domain.user.entity.User;
import teamyc.recordpet.global.image.entity.ProfileImage;

@Getter
@AllArgsConstructor
public class PetRegisterRequest {

    private String name;
    private int age;
    private Gender gender;
    private boolean isNeutered;

    public Pet toEntity(User user, ProfileImage image) {
        return Pet.builder()
            .user(user)
            .name(this.name)
            .age(this.age)
            .gender(this.gender)
            .isNeutered(this.isNeutered)
            .profileImage(image)
            .build();
    }
}
