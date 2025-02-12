package teamyc.recordpet.global.mail;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class EmailVerifyRequest {

    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", message = "이메일 형식에 맞지 않습니다.")
    private final String email;

    public EmailVerifyRequest(String email) {
        this.email = email;
    }
}