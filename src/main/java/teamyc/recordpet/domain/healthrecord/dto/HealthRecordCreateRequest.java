package teamyc.recordpet.domain.healthrecord.dto;

import teamyc.recordpet.domain.healthrecord.entitiy.HealthRecord;

import java.util.List;
import java.util.stream.Collectors;

public record HealthRecordCreateRequest(String title, List<HealthEventCreateRequest> events) {

    public HealthRecord toEntity() {
        return HealthRecord.builder()
                .title(this.title)
                .events(this.events.stream()
                        .map(HealthEventCreateRequest::toEntity)
                        .collect(Collectors.toList())
                )
                .build();
    }
}
