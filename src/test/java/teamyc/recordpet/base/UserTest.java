package teamyc.recordpet.base;

import org.springframework.security.crypto.password.PasswordEncoder;
import teamyc.recordpet.domain.user.entity.User;

public class UserTest {

    PasswordEncoder passwordEncoder;

    protected Long TEST_USER_ID = 1L;
    protected String TEST_USER_NAME = "username";
    String TEST_USER_EMAIL = "username@gmail.com";
    protected String TEST_USER_PASSWORD = "@Abce4!3024821";
    protected String TEST_USER_NEXT_PASSWORD = "!@#Asflsds234";

    protected User TEST_USER = User.builder()
        .userId(TEST_USER_ID)
        .nickname(TEST_USER_NAME)
        .email(TEST_USER_EMAIL)
        .password(TEST_USER_PASSWORD)
        .build();

    protected User TEST_CHANGE_USER = User.builder()
        .userId(TEST_USER_ID)
        .nickname(TEST_USER_NAME)
        .email(TEST_USER_EMAIL)
        .password(TEST_USER_NEXT_PASSWORD)
        .build();
}