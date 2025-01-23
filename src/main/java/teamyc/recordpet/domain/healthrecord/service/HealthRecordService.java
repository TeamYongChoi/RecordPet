package teamyc.recordpet.domain.healthrecord.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamyc.recordpet.domain.healthrecord.dto.HealthRecordCreateRequest;
import teamyc.recordpet.domain.healthrecord.repository.HealthRecordRepository;
import teamyc.recordpet.domain.pet.repository.PetRepository;

@Service
@RequiredArgsConstructor
public class HealthRecordService {

    private final HealthRecordRepository healthRecordRepository;
    private final PetRepository petRepository;

    public void save(Long petId, HealthRecordCreateRequest request) {
        //TODO 유효한 펫 아이디인지 검증하는 구문 작성
        // weightLog 관련 내용 작성 되면 작성하기
        // petRepository.(petId);
        healthRecordRepository.save(request.toEntity());
    }
}
