package teamyc.recordpet.domain.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import teamyc.recordpet.base.UserTest;
import teamyc.recordpet.domain.user.dto.UserChangePasswordRequest;
import teamyc.recordpet.domain.user.dto.UserEditProfileRequest;
import teamyc.recordpet.domain.user.entity.User;
import teamyc.recordpet.domain.user.repository.UserRepository;
import teamyc.recordpet.global.s3.S3Service;

@ExtendWith(MockitoExtension.class)
class UserServiceTest extends UserTest {

    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserService userService;
    @Mock
    PasswordEncoder passwordEncoder;
    @Captor
    ArgumentCaptor<User> argumentCaptor;

    @Mock
    S3Service s3Service;

    @Nested
    @DisplayName("비밀번호 변경 테스트")
    class changePassword {

        @Test
        @DisplayName("비밀번호 변경 성공")
        void changePasswordTest() {
            // given
            UserChangePasswordRequest req = new UserChangePasswordRequest(TEST_USER_PASSWORD,
                TEST_USER_NEXT_PASSWORD);

            given(userRepository.findByUserId(any(Long.class))).willReturn(
                Optional.ofNullable(TEST_USER));
            given(userRepository.save(any(User.class))).willReturn(TEST_UPDATED_USER);
            given(passwordEncoder.matches(req.getCurrentPassword(), TEST_USER_PASSWORD)).willReturn(
                true);
            given(passwordEncoder.encode(TEST_USER_NEXT_PASSWORD)).willReturn(
                TEST_UPDATED_USER.getPassword());

            // when
            userService.changePassword(TEST_USER_ID, req);

            // then
            verify(userRepository).save(any(User.class));
            verify(userRepository).findByUserId(any(Long.class));
            verify(userRepository).save(argumentCaptor.capture());
            assertEquals(TEST_USER_NEXT_PASSWORD, TEST_UPDATED_USER.getPassword());
            assertEquals(TEST_USER_ID, argumentCaptor.getValue().getUserId());
        }
    }

    @Nested
    @DisplayName("사용자 프로필 편집 테스트")
    class editUserProfile {

        @Test
        @DisplayName("사용자 프로필 편집 성공-multipartFile이 비어있는 경우")
        void editUserProfileSuccess_EmptyMultipartFile() {
            // given
            UserEditProfileRequest req = UserEditProfileRequest.builder()
                .nickname(TEST_USER_NAME)
                .build();

            MockMultipartFile mockMultipartFile = new MockMultipartFile("image", "", "",
                new byte[0]);

            given(userRepository.findByUserId(anyLong())).willReturn(
                Optional.ofNullable(TEST_USER));
            given(userRepository.existsByNickname(anyString())).willReturn(false);

            // when
            userService.editProfile(TEST_USER_ID, req, mockMultipartFile);

            // then
            verify(userRepository).findByUserId(TEST_USER_ID);
            verify(userRepository).save(any());
        }

        @Test
        @DisplayName("사용자 프로필 편집 성공-multipartFile 업로드-기존 프로필 있는 경우")
        void editUserProfileSuccess_NotEmptyMultipartFile_ExistProfileImage() {
            // given
            UserEditProfileRequest req = UserEditProfileRequest.builder()
                .nickname(TEST_USER_NAME)
                .build();
            MockMultipartFile mockMultipartFile = new MockMultipartFile("image", "profile.jpg",
                "image/jpeg", "fake image".getBytes());

            given(userRepository.findByUserId(anyLong())).willReturn(
                Optional.ofNullable(TEST_USER));
            given(s3Service.uploadImage(any(), any())).willReturn(TEST_UPDATED_USER_PROFILE_IMAGE);
            given(userRepository.existsByNickname(anyString())).willReturn(false);

            // when
            userService.editProfile(TEST_USER_ID, req, mockMultipartFile);

            // then
            verify(userRepository).findByUserId(TEST_USER_ID);
            verify(userRepository).save(any());
            verify(s3Service).deleteFile(any());
            verify(s3Service).uploadImage(mockMultipartFile, "user-profile-images");
        }

        @Test
        @DisplayName("사용자 프로필 편집 성공-multipartFile 업로드-기존 프로필 없는 경우")
        void editUserProfileSuccess_NotEmptyMultipartFile_NoExistProfileImage() {
            // given
            UserEditProfileRequest req = UserEditProfileRequest.builder()
                .nickname(TEST_USER_NAME)
                .build();
            MockMultipartFile mockMultipartFile = new MockMultipartFile("image", "profile.jpg",
                "image/jpeg", "fake image".getBytes());

            given(userRepository.findByUserId(anyLong())).willReturn(
                Optional.ofNullable(TEST_NO_PROFILE_IMAGE_USER));
            given(s3Service.uploadImage(any(), eq("user-profile-images"))).willReturn(
                TEST_UPDATED_USER_PROFILE_IMAGE);
            given(userRepository.existsByNickname(anyString())).willReturn(false);

            // when
            userService.editProfile(TEST_USER_ID, req, mockMultipartFile);

            // then
            verify(userRepository).findByUserId(TEST_USER_ID);
            verify(userRepository).save(any());
            verify(s3Service, never()).deleteFile(any());
            verify(s3Service).uploadImage(mockMultipartFile, "user-profile-images");
        }
    }
}