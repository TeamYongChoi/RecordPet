package teamyc.recordpet.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetUserProfileResponse {

    private final String nickname;
    private final String profileImageUrl;

    @Builder
    public GetUserProfileResponse(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}