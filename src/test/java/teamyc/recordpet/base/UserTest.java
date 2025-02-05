package teamyc.recordpet.base;

import teamyc.recordpet.domain.user.entity.User;

public class UserTest {

    protected Long TEST_USER_ID = 1L;
    protected String TEST_USER_NAME = "username";
    String TEST_USER_EMAIL = "username@gmail.com";
    protected String TEST_USER_PASSWORD = "@Abce4!3024821";
    protected String TEST_USER_NEXT_PASSWORD = "!@#Asflsds234";
    protected String TEST_USER_PROFILE_IMAGE = "test_image_url";
    protected String TEST_UPDATED_USER_PROFILE_IMAGE = "test_updated_image_url";

    protected User TEST_USER = User.builder()
        .userId(TEST_USER_ID)
        .nickname(TEST_USER_NAME)
        .email(TEST_USER_EMAIL)
        .password(TEST_USER_PASSWORD)
        .userProfileImageUrl(TEST_USER_PROFILE_IMAGE)
        .build();

    protected User TEST_UPDATED_USER = User.builder()
        .userId(TEST_USER_ID)
        .nickname(TEST_USER_NAME)
        .email(TEST_USER_EMAIL)
        .password(TEST_USER_NEXT_PASSWORD)
        .userProfileImageUrl(TEST_UPDATED_USER_PROFILE_IMAGE)
        .build();

    protected User TEST_NO_PROFILE_IMAGE_USER = User.builder()
        .userId(TEST_USER_ID)
        .nickname(TEST_USER_NAME)
        .email(TEST_USER_EMAIL)
        .password(TEST_USER_PASSWORD)
        .build();
}