package teamyc.recordpet.domain.healthrecord.dto;

import lombok.Builder;
import teamyc.recordpet.domain.healthrecord.entitiy.HealthEvent;
import teamyc.recordpet.domain.healthrecord.entitiy.HealthRecord;

import java.util.List;

public record HealthRecordResponse(String title,
                                   List<HealthEvent> events) {

    @Builder
    public HealthRecordResponse(String title, List<HealthEvent> events){
        this.title = title;
        this.events = events;
    }

    public static HealthRecordResponse fromEntity(HealthRecord healthRecord){
        return HealthRecordResponse.builder()
                .title(healthRecord.getTitle())
                .events(healthRecord.getEvents())
                .build();
    }
}
