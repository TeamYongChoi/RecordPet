package teamyc.recordpet.domain.user.service;

import static teamyc.recordpet.global.exception.ResultCode.DUPLICATE_USER_EMAIL;
import static teamyc.recordpet.global.exception.ResultCode.DUPLICATE_USER_NICKNAME;
import static teamyc.recordpet.global.exception.ResultCode.NOT_ACCEPTABLE_BLANK;
import static teamyc.recordpet.global.exception.ResultCode.NOT_FOUND_USER;
import static teamyc.recordpet.global.exception.ResultCode.NOT_MATCH_PASSWORD;
import static teamyc.recordpet.global.exception.ResultCode.UNAUTHORIZED_EMAIL;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamyc.recordpet.domain.user.dto.UserChangePasswordRequest;
import teamyc.recordpet.domain.user.dto.UserChangePasswordResponse;
import teamyc.recordpet.domain.user.dto.UserSignupRequest;
import teamyc.recordpet.domain.user.dto.UserSignupResponse;
import teamyc.recordpet.domain.user.entity.User;
import teamyc.recordpet.domain.user.repository.UserRepository;
import teamyc.recordpet.global.SendMailResponse;
import teamyc.recordpet.global.exception.GlobalException;
import teamyc.recordpet.global.mail.ConfirmMailResponse;
import teamyc.recordpet.global.mail.EmailVerifyRequest;
import teamyc.recordpet.global.mail.service.EmailAuthService;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailAuthService emailAuthService;

    public UserSignupResponse signup(UserSignupRequest req) {
        // 중복 이메일 체크 (이미 가입된 사람인지 체크)
        checkDuplicateEmail(req);
        // 이메일 인증 완료 여부 체크
        if (!emailAuthService.findById(req.getEmail()).isChecked()) {
            throw new GlobalException(UNAUTHORIZED_EMAIL);
        }
        // 닉네임 중복 체크
        checkDuplicateNickname(req);
        // 비밀번호 암호화
        String pw = passwordEncoder.encode(req.getPassword());

        User user = req.toEntity(pw);
        userRepository.save(user);

        return UserSignupResponse.fromEntity(user);
    }

    public SendMailResponse sendMail(EmailVerifyRequest req) {
        emailAuthService.sendMessage(req.getEmail());
        return SendMailResponse.builder().build();
    }

    public ConfirmMailResponse confirmMail(String email, String code) {
        emailAuthService.checkCode(email, code);
        return ConfirmMailResponse.builder().email(email).build();
    }

    @Transactional
    public UserChangePasswordResponse changePassword(Long userId, UserChangePasswordRequest req) {
        if (req.getCurrentPassword() == null || req.getNextPassword() == null) {
            throw new GlobalException(NOT_ACCEPTABLE_BLANK);
        }
        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new GlobalException(NOT_FOUND_USER));

        if (!passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())) {
            throw new GlobalException(NOT_MATCH_PASSWORD);
        }

        String newPassword = passwordEncoder.encode(req.getNextPassword());

        User updateUser = User.builder()
            .userId(userId)
            .nickname(user.getNickname())
            .email(user.getEmail())
            .password(newPassword)
            .role(user.getRole())
            .userProfileImageUrl(user.getUserProfileImageUrl())
            .build();

        userRepository.save(updateUser);

        return new UserChangePasswordResponse();
    }

    private void checkDuplicateEmail(UserSignupRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new GlobalException(DUPLICATE_USER_EMAIL);
        }
    }

    private void checkDuplicateNickname(UserSignupRequest req) {
        if (userRepository.existsByNickname(req.getNickname())) {
            throw new GlobalException(DUPLICATE_USER_NICKNAME);
        }
    }
}