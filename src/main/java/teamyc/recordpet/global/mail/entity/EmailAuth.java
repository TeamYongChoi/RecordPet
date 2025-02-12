package teamyc.recordpet.global.mail.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "auth", timeToLive = 300)
public class EmailAuth {

    @Id
    private final String email;
    private final String code;
    private boolean isChecked = false;

    @Builder
    public EmailAuth(String email, String code, boolean isChecked) {
        this.email = email;
        this.code = code;
        this.isChecked = isChecked;
    }
}
