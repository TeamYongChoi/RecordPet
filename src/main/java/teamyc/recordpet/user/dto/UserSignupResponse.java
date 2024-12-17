package teamyc.recordpet.user.dto;

import lombok.Builder;
import lombok.Getter;
import teamyc.recordpet.user.entity.Role;
import teamyc.recordpet.user.entity.User;

@Getter
public class UserSignupResponse {

    private final Long userId;
    private final String nickname;
    private final String email;
    private final Role role;

    @Builder
    private UserSignupResponse(Long userId, String nickname, String email, Role role) {
        this.userId = userId;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
    }

    public static UserSignupResponse from(User user) {
        return UserSignupResponse.builder()
            .userId(user.getUserId())
            .email(user.getEmail())
            .nickname(user.getNickname())
            .role(user.getRole())
            .build();
    }
}