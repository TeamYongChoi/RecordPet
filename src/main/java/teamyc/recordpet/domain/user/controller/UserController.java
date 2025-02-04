package teamyc.recordpet.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import teamyc.recordpet.domain.user.dto.GetUserProfileResponse;
import teamyc.recordpet.domain.user.dto.UserChangePasswordRequest;
import teamyc.recordpet.domain.user.dto.UserChangePasswordResponse;
import teamyc.recordpet.domain.user.dto.UserEditProfileRequest;
import teamyc.recordpet.domain.user.dto.UserEditProfileResponse;
import teamyc.recordpet.domain.user.dto.UserSignupRequest;
import teamyc.recordpet.domain.user.dto.UserSignupResponse;
import teamyc.recordpet.domain.user.service.UserService;
import teamyc.recordpet.global.SendMailResponse;
import teamyc.recordpet.global.exception.CustomResponse;
import teamyc.recordpet.global.mail.ConfirmMailResponse;
import teamyc.recordpet.global.mail.EmailVerifyRequest;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public CustomResponse<UserSignupResponse> signup(@RequestBody @Valid UserSignupRequest req) {
        return CustomResponse.success(userService.signup(req));
    }

    @PostMapping("/auth/email")
    public CustomResponse<SendMailResponse> sendMail(@RequestBody @Valid EmailVerifyRequest req) {
        return CustomResponse.success(userService.sendMail(req));
    }

    @GetMapping("/auth/email/verify")
    public CustomResponse<ConfirmMailResponse> confirmMail(
        @RequestParam(name = "email") String email, @RequestParam(name = "authCode") String code) {
        return CustomResponse.success(userService.confirmMail(email, code));
    }

    @PatchMapping("/{userId}/change-password")
    public CustomResponse<UserChangePasswordResponse> changePassword(@PathVariable Long userId,
        @RequestBody UserChangePasswordRequest req) {
        return CustomResponse.success(userService.changePassword(userId, req));
    }

    @PatchMapping("/{userId}/edit-profile")
    public CustomResponse<UserEditProfileResponse> editProfile(@PathVariable Long userId,
        @RequestPart("data") UserEditProfileRequest req,
        @RequestPart("image") MultipartFile multipartFile) {
        return CustomResponse.success(userService.editProfile(userId, req, multipartFile));
    }

    @GetMapping("{userId}/profile")
    public CustomResponse<GetUserProfileResponse> getProfile(@PathVariable Long userId) {
        return CustomResponse.success(userService.getProfile(userId));
    }
}
