package teamyc.recordpet.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import teamyc.recordpet.domain.user.entity.User;

@Getter
public class GetUserProfileResponse {

    private final String nickname;
    private final String profileImageUrl;

    @Builder
    public GetUserProfileResponse(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    public static GetUserProfileResponse fromEntity(User user) {
        String imageUrl = user.getProfileUrl();
        return GetUserProfileResponse.builder()
            .nickname(user.getNickname())
            .profileImageUrl(imageUrl)
            .build();
    }
}