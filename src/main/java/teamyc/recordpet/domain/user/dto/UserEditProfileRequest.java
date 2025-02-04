package teamyc.recordpet.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserEditProfileRequest {

    private final String nickname;

    @Builder
    public UserEditProfileRequest(String nickname) {
        this.nickname = nickname;
    }
}