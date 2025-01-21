package teamyc.recordpet.domain.symptom.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamyc.recordpet.domain.pet.repository.PetRepository;
import teamyc.recordpet.domain.symptom.dto.SymptomCreateRequest;
import teamyc.recordpet.domain.symptom.repository.SymptomRepository;

@Service
@RequiredArgsConstructor
public class SymptomService {

    private final SymptomRepository symptomRepository;
    private final PetRepository petRepository;

    public void save(Long petId, SymptomCreateRequest request) {
        //TODO 유효한 펫 아이디인지 검증하는 구문 작성
        // petRepository.(petId);
        symptomRepository.save(request.toEntity());
    }
}
