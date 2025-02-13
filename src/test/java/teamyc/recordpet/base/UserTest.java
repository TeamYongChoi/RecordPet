package teamyc.recordpet.base;

import teamyc.recordpet.domain.user.entity.User;
import teamyc.recordpet.global.image.entity.ProfileImage;
import teamyc.recordpet.global.image.entity.Type;

public class UserTest {

    protected Long TEST_USER_ID = 1L;
    protected String TEST_USER_NAME = "username";
    String TEST_USER_EMAIL = "username@gmail.com";
    protected String TEST_USER_PASSWORD = "@Abce4!3024821";
    protected String TEST_USER_NEXT_PASSWORD = "!@#Asflsds234";
    protected ProfileImage TEST_USER_BASIC_PROFILE_IMAGE = ProfileImage.builder()
            .id(1L)
            .imageUrl("test_image_url")
            .isBasic(true)
            .type(Type.USER)
            .build();

    protected ProfileImage TEST_USER_CUSTOM_PROFILE_IMAGE = ProfileImage.builder()
            .id(1L)
            .imageUrl("test_custom_image_url")
            .isBasic(false)
            .type(Type.USER)
            .build();

    protected User TEST_BASIC_IMAGE_USER = User.builder()
            .id(TEST_USER_ID)
            .nickname(TEST_USER_NAME)
            .email(TEST_USER_EMAIL)
            .password(TEST_USER_PASSWORD)
            .profileImage(TEST_USER_BASIC_PROFILE_IMAGE)
            .build();

    protected User TEST_CUSTOM_IMAGE_USER = User.builder()
            .id(TEST_USER_ID)
            .nickname(TEST_USER_NAME)
            .email(TEST_USER_EMAIL)
            .password(TEST_USER_PASSWORD)
            .profileImage(TEST_USER_CUSTOM_PROFILE_IMAGE)
            .build();


    protected User TEST_UPDATED_USER = User.builder()
            .id(TEST_USER_ID)
            .nickname(TEST_USER_NAME)
            .email(TEST_USER_EMAIL)
            .password(TEST_USER_NEXT_PASSWORD)
            .profileImage(TEST_USER_CUSTOM_PROFILE_IMAGE)
            .build();
}