package teamyc.recordpet.domain.user.service;

import static teamyc.recordpet.global.exception.ResultCode.DUPLICATE_USER_EMAIL;
import static teamyc.recordpet.global.exception.ResultCode.DUPLICATE_USER_NICKNAME;
import static teamyc.recordpet.global.exception.ResultCode.NOT_ACCEPTABLE_NICKNAME_BLANK;
import static teamyc.recordpet.global.exception.ResultCode.NOT_ACCEPTABLE_PASSWORD_BLANK;
import static teamyc.recordpet.global.exception.ResultCode.NOT_FOUND_USER;
import static teamyc.recordpet.global.exception.ResultCode.NOT_MATCH_PASSWORD;
import static teamyc.recordpet.global.exception.ResultCode.UNAUTHORIZED_EMAIL;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import teamyc.recordpet.domain.user.dto.GetUserProfileResponse;
import teamyc.recordpet.domain.user.dto.UserChangePasswordRequest;
import teamyc.recordpet.domain.user.dto.UserChangePasswordResponse;
import teamyc.recordpet.domain.user.dto.UserEditProfileRequest;
import teamyc.recordpet.domain.user.dto.UserEditProfileResponse;
import teamyc.recordpet.domain.user.dto.UserSignupRequest;
import teamyc.recordpet.domain.user.dto.UserSignupResponse;
import teamyc.recordpet.domain.user.entity.User;
import teamyc.recordpet.domain.user.repository.UserRepository;
import teamyc.recordpet.global.SendMailResponse;
import teamyc.recordpet.global.exception.GlobalException;
import teamyc.recordpet.global.mail.ConfirmMailResponse;
import teamyc.recordpet.global.mail.EmailVerifyRequest;
import teamyc.recordpet.global.mail.service.EmailAuthService;
import teamyc.recordpet.global.s3.S3Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailAuthService emailAuthService;
    private final S3Service s3Service;

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
            throw new GlobalException(NOT_ACCEPTABLE_PASSWORD_BLANK);
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

    @Transactional
    public UserEditProfileResponse editProfile(Long userId, UserEditProfileRequest req,
        MultipartFile multipartFile) {
        if (req.getNickname() == null) {
            throw new GlobalException(NOT_ACCEPTABLE_NICKNAME_BLANK);
        }

        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new GlobalException(NOT_FOUND_USER));

        // multipartFile이 비어있지 않은 경우 -> 프로필 이미지 업로드 하는 경우
        if (!multipartFile.isEmpty()) {
            // 이미 유저가 기존 프로필 이미지를 가지고 있는 경우
            if (user.getUserProfileImageUrl() != null) {
                s3Service.deleteFile(user.getUserProfileImageUrl());
            }
            String newProfileImageUrl = uploadProfileImage(multipartFile);

            User updatedUser = User.builder()
                .userId(userId)
                .nickname(req.getNickname())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .userProfileImageUrl(newProfileImageUrl)
                .build();

            userRepository.save(updatedUser);

            return new UserEditProfileResponse();
        }

        // multipartFile이 비어있는 경우 -> 프로필 이미지 업로드 안 하는 경우
        User updatedUser = User.builder()
            .userId(userId)
            .nickname(req.getNickname())
            .email(user.getEmail())
            .password(user.getPassword())
            .role(user.getRole())
            .userProfileImageUrl(user.getUserProfileImageUrl())
            .build();

        userRepository.save(updatedUser);

        return new UserEditProfileResponse();
    }

    public GetUserProfileResponse getProfile(Long userId) {
        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new GlobalException(NOT_FOUND_USER));

        return GetUserProfileResponse.builder()
            .nickname(user.getNickname())
            .profileImageUrl(user.getUserProfileImageUrl())
            .build();
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

    private String uploadProfileImage(MultipartFile profileImage) {
        return s3Service.uploadImage(profileImage, "user-profile-images");
    }
}