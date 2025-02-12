package teamyc.recordpet.global.mail;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ConfirmMailResponse {

    private final String email;

    @Builder
    public ConfirmMailResponse(String email) {
        this.email = email;
    }
}