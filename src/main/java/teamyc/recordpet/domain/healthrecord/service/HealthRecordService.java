package teamyc.recordpet.domain.healthrecord.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamyc.recordpet.domain.healthrecord.dto.*;
import teamyc.recordpet.domain.healthrecord.entitiy.HealthEvent;
import teamyc.recordpet.domain.healthrecord.entitiy.HealthRecord;
import teamyc.recordpet.domain.healthrecord.repository.HealthRecordRepository;
import teamyc.recordpet.domain.pet.entity.Pet;
import teamyc.recordpet.domain.pet.repository.PetRepository;
import teamyc.recordpet.global.exception.GlobalException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static teamyc.recordpet.global.exception.ResultCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class HealthRecordService {

    private final HealthRecordRepository healthRecordRepository;
    private final PetRepository petRepository;

    public void save(Long petId, HealthRecordCreateRequest request) {

        Pet pet = petRepository.findById(petId).orElseThrow(() -> new GlobalException(NOT_FOUND_PET_PROFILE));

        HealthRecord healthRecord = HealthRecord.builder()
                .pet(pet)
                .title(request.title())
                .build();

        request.events().forEach(eventRequest ->
                healthRecord.addEvent(eventRequest.toEntity())
        );

        healthRecordRepository.save(healthRecord);
    }

    @Transactional(readOnly = true)
    public List<MonthlyHealthRecordResponse> findByMonthAndPetId(Long petId, int year, int month) {
        if (!petRepository.existsById(petId)) {
            throw new GlobalException(NOT_FOUND_PET_PROFILE);
        }


        List<HealthRecord> result = healthRecordRepository.findByMonthAndPetId(petId, year, month);
        log.info("조회된 건강 기록 개수 : {}", result.size());

        return result
                .stream()
                .map(MonthlyHealthRecordResponse::fromEntity)
                .toList();
    }

    public Page<HealthRecordResponse> findAllHealthRecords(Long petId, Pageable pageable) {
        if (!petRepository.existsById(petId)) {
            throw new GlobalException(NOT_FOUND_PET_PROFILE);
        }
        return healthRecordRepository.findAllByPetId(petId, pageable)
                .map(HealthRecordResponse::fromEntity);
    }

    public void updateHealthRecord(Long petId, Long recordId, HealthRecordUpdateRequest request) {
        HealthRecord healthRecord = healthRecordRepository.findById(recordId)
                .orElseThrow(() -> new GlobalException(NOT_FOUND_HEALTH_RECORD));

        if (!isEquals(petId, healthRecord)) {
            throw new GlobalException(PET_MISMATCH);
        }

        updateTitleIfChanged(healthRecord, request);
        updateEvents(healthRecord, request);
    }

    @Transactional
    public void deleteHealthRecord(Long petId, Long recordId) {
        HealthRecord healthRecord = healthRecordRepository.findByIdAndPetId(recordId, petId)
                .orElseThrow(() -> new GlobalException(NOT_FOUND_HEALTH_RECORD));

        healthRecordRepository.delete(healthRecord);
    }

    @Transactional
    public void deleteHealthEvent(Long petId, Long recordId, Long eventId) {
        HealthRecord record = healthRecordRepository.findByIdAndPetId(recordId, petId)
                .orElseThrow(() -> new GlobalException(NOT_FOUND_HEALTH_RECORD));

        HealthEvent event = record.getEvents().stream()
                .filter(e -> e.getId().equals(eventId))
                .findFirst()
                .orElseThrow(() -> new GlobalException(NOT_FOUND_HEALTH_EVENT));

        record.removeEvent(event);
    }

    private boolean isEquals(Long petId, HealthRecord healthRecord) {
        return healthRecord.getPet().getId().equals(petId);
    }

    private static void updateTitleIfChanged(HealthRecord healthRecord, HealthRecordUpdateRequest request) {
        if (!healthRecord.getTitle().equals(request.title())) {
            healthRecord.updateTitle(request.title());
        }
    }

    private static void updateEvents(HealthRecord healthRecord, HealthRecordUpdateRequest request) {
        Map<Long, HealthEvent> existingEvents = getExistingEvents(healthRecord);
        List<HealthEvent> updatedEvents = new ArrayList<>();

        for (HealthEventUpdateRequest eventRequest : request.events()) {
            if (eventRequest.id() == null) {
                // 신규 이벤트 추가
                updatedEvents.add(eventRequest.toEntity(healthRecord));
            } else if (existingEvents.containsKey(eventRequest.id())) {
                // 기존 이벤트 수정
                updatedEvents.add(updateExistingEvent(existingEvents, eventRequest));
            }
        }
        removeDeletedEvents(healthRecord, existingEvents);
        healthRecord.updateEvents(updatedEvents);
    }

    private static HealthEvent updateExistingEvent(Map<Long, HealthEvent> existingEvents, HealthEventUpdateRequest eventRequest) {
        HealthEvent existingEvent = existingEvents.get(eventRequest.id());
        existingEvent.update(eventRequest.occurrenceTime(), eventRequest.content());
        existingEvents.remove(eventRequest.id()); // 남은 이벤트는 삭제 대상
        return existingEvent;
    }

    private static Map<Long, HealthEvent> getExistingEvents(HealthRecord healthRecord) {
        return healthRecord.getEvents().stream()
                .collect(Collectors.toMap(HealthEvent::getId, event -> event));
    }

    private static void removeDeletedEvents(HealthRecord healthRecord, Map<Long, HealthEvent> existingEvents) {
        existingEvents.values().forEach(healthRecord::removeEvent);
    }


}
