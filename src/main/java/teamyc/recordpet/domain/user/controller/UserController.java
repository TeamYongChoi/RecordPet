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
import org.springframework.web.bind.annotation.RestController;
import teamyc.recordpet.domain.user.dto.UserChangePasswordRequest;
import teamyc.recordpet.domain.user.dto.UserChangePasswordResponse;
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
}
