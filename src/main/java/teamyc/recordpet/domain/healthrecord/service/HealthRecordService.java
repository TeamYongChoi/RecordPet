package teamyc.recordpet.domain.healthrecord.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamyc.recordpet.domain.healthrecord.dto.HealthRecordCreateRequest;
import teamyc.recordpet.domain.healthrecord.dto.MonthlyHealthRecordResponse;
import teamyc.recordpet.domain.healthrecord.repository.HealthRecordRepository;
import teamyc.recordpet.domain.pet.repository.PetRepository;
import teamyc.recordpet.global.exception.GlobalException;
import teamyc.recordpet.global.exception.ResultCode;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthRecordService {

    private final HealthRecordRepository healthRecordRepository;
    private final PetRepository petRepository;

    public void save(Long petId, HealthRecordCreateRequest request) {

        if (!petRepository.existsById(petId)) {
            throw new GlobalException(ResultCode.NOT_FOUND_PET_PROFILE);
        }

        healthRecordRepository.save(request.toEntity());
    }

    public List<MonthlyHealthRecordResponse> findByMonthAndPetId(Long petId, int year, int month) {
        if (!petRepository.existsById(petId)) {
            throw new GlobalException(ResultCode.NOT_FOUND_PET_PROFILE);
        }

        return healthRecordRepository.findByMonthAndPetId(petId, year, month)
                .stream()
                .map(MonthlyHealthRecordResponse::fromEntity)
                .toList();
    }
}
