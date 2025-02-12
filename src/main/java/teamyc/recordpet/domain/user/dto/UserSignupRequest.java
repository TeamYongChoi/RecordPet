package teamyc.recordpet.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import teamyc.recordpet.domain.user.entity.Role;
import teamyc.recordpet.domain.user.entity.User;

@Getter
@AllArgsConstructor
public class UserSignupRequest {

    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", message = "이메일 형식에 맞지 않습니다.")
    private final String email;
    @NotBlank(message = "닉네임을 입력해주세요.")
    private final String nickname;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])(?=.*[0-9]).{10,16}$", message = "비밀번호는 특수문자, 대소문자 포함 10자-16자 이내여야 합니다.")
    private final String password;

    public User toEntity(String encodedPassword) {
        return User.builder()
            .email(this.email)
            .nickname(this.nickname)
            .password(encodedPassword)
            .role(Role.MEMBER)
            .build();
    }
}
