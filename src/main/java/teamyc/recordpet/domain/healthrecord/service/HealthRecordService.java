package teamyc.recordpet.domain.healthrecord.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import teamyc.recordpet.domain.healthrecord.dto.HealthRecordCreateRequest;
import teamyc.recordpet.domain.healthrecord.dto.HealthRecordResponse;
import teamyc.recordpet.domain.healthrecord.dto.MonthlyHealthRecordResponse;
import teamyc.recordpet.domain.healthrecord.repository.HealthRecordRepository;
import teamyc.recordpet.domain.pet.repository.PetRepository;
import teamyc.recordpet.global.exception.GlobalException;

import java.util.List;

import static teamyc.recordpet.global.exception.ResultCode.NOT_FOUND_PET_PROFILE;

@Service
@RequiredArgsConstructor
public class HealthRecordService {

    private final HealthRecordRepository healthRecordRepository;
    private final PetRepository petRepository;

    public void save(Long petId, HealthRecordCreateRequest request) {

        Pet pet = petRepository.findById(petId).orElseThrow(() -> new GlobalException(NOT_FOUND_PET_PROFILE));

        HealthRecord healthRecord = HealthRecord.builder()
                .pet(pet)
                .title(request.title())
                .build();

        healthRecordRepository.save(request.toEntity());
    }

    public List<MonthlyHealthRecordResponse> findByMonthAndPetId(Long petId, int year, int month) {
        if (!petRepository.existsById(petId)) {
            throw new GlobalException(NOT_FOUND_PET_PROFILE);
        }

        return healthRecordRepository.findByMonthAndPetId(petId, year, month)
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
}
