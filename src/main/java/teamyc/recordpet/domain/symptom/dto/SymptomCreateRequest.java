package teamyc.recordpet.domain.symptom.dto;

import teamyc.recordpet.domain.symptom.entitiy.Symptom;

import java.time.LocalDateTime;

public record SymptomCreateRequest(LocalDateTime createdAt, String description){

    public Symptom toEntity() {
        return Symptom.builder()
                .createdAt(createdAt)
                .description(description)
                .build();
    }
}
