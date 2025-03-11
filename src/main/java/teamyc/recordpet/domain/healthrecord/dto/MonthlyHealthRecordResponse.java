package teamyc.recordpet.domain.healthrecord.dto;

import lombok.Builder;
import teamyc.recordpet.domain.healthrecord.entitiy.HealthRecord;

public record MonthlyHealthRecordResponse(
        String title) {

    @Builder
    public MonthlyHealthRecordResponse(String title) {
        this.title = title;
    }

    public static MonthlyHealthRecordResponse fromEntity(HealthRecord healthRecord) {
        return MonthlyHealthRecordResponse.builder()
                .title(healthRecord.getTitle())
                .build();
    }
}
