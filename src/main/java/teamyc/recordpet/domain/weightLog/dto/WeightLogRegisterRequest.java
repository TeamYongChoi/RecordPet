package teamyc.recordpet.domain.weightLog.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record WeightLogRegisterRequest(
    @NotNull(message = "애완동물 식별자는 필수 요소입니다.") Long petId,
    @NotNull(message = "몸무게는 필수 입력값입니다.") @Min(value = 0, message = "몸무게는 0보다 큰 값을 입력해야 합니다.") Double weight,
    LocalDate date) {

    public WeightLogRegisterRequest(Long petId, Double weight, LocalDate date) {
        this.petId = petId;
        this.weight = weight;
        this.date = date != null ? date : LocalDate.now();
    }
}
