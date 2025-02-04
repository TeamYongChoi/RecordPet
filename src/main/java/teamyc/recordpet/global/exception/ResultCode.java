package teamyc.recordpet.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResultCode {
    // Success
    SUCCESS("Success", "정상 처리 되었습니다.", HttpStatus.OK),
    CREATED("Created", "등록에 성공했습니다.", HttpStatus.CREATED),

    // Fail
    SYSTEM_ERROR("System Error", "시스템 내부 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // User U-
    DUPLICATE_USER_EMAIL("U001", "이미 가입된 이메일 입니다.", HttpStatus.CONFLICT),
    DUPLICATE_USER_NICKNAME("U002", "중복된 닉네임 입니다.", HttpStatus.CONFLICT),
    NOT_FOUND_USER("U003", "존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND),
    NOT_MATCH_PASSWORD("U004", "비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    NOT_ACCEPTABLE_PASSWORD_BLANK("U005", "이전 비밀번호와 변경할 비밀번호는 반드시 입력해야 합니다.",
        HttpStatus.BAD_REQUEST),
    NOT_ACCEPTABLE_NICKNAME_BLANK("U006", "닉네임은 반드시 입력해야 합니다.", HttpStatus.BAD_REQUEST),
    EMAIL_SEND_FAILED("UE001", "이메일 전송에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_CODE("UE002", "인증 코드가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_EMAIL_AUTH("UE003", "인증 정보가 존재하지 않습니다. 다시 시도하세요.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_EMAIL("UE004", "인증 완료되지 않은 email 입니다.", HttpStatus.UNAUTHORIZED),

    // Pet P-
    NOT_FOUND_PET_PROFILE("P001", "조회된 프로필이 없습니다.", HttpStatus.NOT_FOUND),

    // S3 S-
    IMAGE_UPLOAD_FAIL("S001", "이미지 업로드 실패", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}