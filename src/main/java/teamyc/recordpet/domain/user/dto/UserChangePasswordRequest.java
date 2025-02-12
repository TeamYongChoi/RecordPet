package teamyc.recordpet.domain.user.dto;

import lombok.Getter;

@Getter
public class UserChangePasswordRequest {

    private final String currentPassword;
    private final String nextPassword;

    public UserChangePasswordRequest(String currentPassword, String nextPassword) {
        this.currentPassword = currentPassword;
        this.nextPassword = nextPassword;
    }
}