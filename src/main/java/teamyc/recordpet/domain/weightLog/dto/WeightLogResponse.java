package teamyc.recordpet.domain.weightLog.dto;

import lombok.Builder;
import teamyc.recordpet.domain.weightLog.entity.WeightLog;

import java.time.LocalDate;

@Builder
public record WeightLogResponse(LocalDate date, Double weight) {
    public static WeightLogResponse fromEntity(WeightLog weightLog) {
        return WeightLogResponse.builder()
                .date(weightLog.getDate())
                .weight(weightLog.getWeight())
                .build();
    }
}
