package teamyc.recordpet.domain.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import teamyc.recordpet.base.UserTest;
import teamyc.recordpet.domain.user.dto.UserChangePasswordRequest;
import teamyc.recordpet.domain.user.entity.User;
import teamyc.recordpet.domain.user.repository.UserRepository;

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
            given(userRepository.save(any(User.class))).willReturn(TEST_CHANGE_USER);
            given(passwordEncoder.matches(req.getCurrentPassword(), TEST_USER_PASSWORD)).willReturn(
                true);
            given(passwordEncoder.encode(TEST_USER_NEXT_PASSWORD)).willReturn(
                TEST_CHANGE_USER.getPassword());

            // when
            userService.changePassword(TEST_USER_ID, req);

            // then
            verify(userRepository).save(any(User.class));
            verify(userRepository).findByUserId(any(Long.class));
            verify(userRepository).save(argumentCaptor.capture());
            assertEquals(TEST_USER_NEXT_PASSWORD, TEST_CHANGE_USER.getPassword());
            assertEquals(TEST_USER_ID, argumentCaptor.getValue().getUserId());
        }
    }
}