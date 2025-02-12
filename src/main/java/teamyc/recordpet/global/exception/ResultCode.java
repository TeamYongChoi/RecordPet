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

    // User U-
    DUPLICATE_USER_EMAIL("U001", "이미 가입된 이메일 입니다.", HttpStatus.CONFLICT),

    // Pet P-
    NOT_FOUND_PET_PROFILE("P001","조회된 프로필이 없습니다.",HttpStatus.NOT_FOUND),

    // S3 S-
    IMAGE_UPLOAD_FAIL("S001", "이미지 업로드 실패", HttpStatus.INTERNAL_SERVER_ERROR),

    // HealthRecord H-
    NOT_FOUND_HEALTH_RECORD("H001","조회된 건강기록이 없습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_HEALTH_EVENT("H002", "조회된 건강기록 이벤트가 없습니다.", HttpStatus.NOT_FOUND),
    PET_MISMATCH("H003","해당 애완동물의 건강기록이 아닙니다.", HttpStatus.UNAUTHORIZED);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}