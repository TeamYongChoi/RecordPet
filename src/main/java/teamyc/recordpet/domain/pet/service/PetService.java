package teamyc.recordpet.domain.pet.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import teamyc.recordpet.domain.pet.dto.PetRegisterRequest;
import teamyc.recordpet.domain.pet.dto.PetResponse;
import teamyc.recordpet.domain.pet.dto.PetUpdateRequest;
import teamyc.recordpet.domain.pet.dto.PetUpdateResponse;
import teamyc.recordpet.domain.pet.entity.Pet;
import teamyc.recordpet.domain.pet.repository.PetRepository;
import teamyc.recordpet.global.exception.GlobalException;
import teamyc.recordpet.global.s3.S3Service;

import java.util.List;
import java.util.stream.Collectors;

import static teamyc.recordpet.global.exception.ResultCode.NOT_FOUND_PET_PROFILE;

@RequiredArgsConstructor
@Service
@Slf4j
public class PetService {

    private final PetRepository petRepository;
    private final S3Service s3Service;

    //조회
    public List<PetResponse> PetProfileFindAll() {
        return petRepository.findAll().stream()
                .map(PetResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public PetResponse PetProfileFindById(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new GlobalException(NOT_FOUND_PET_PROFILE));
        return PetResponse.fromEntity(pet);
    }

    //등록
    public PetResponse PetProfileSave(PetRegisterRequest req, MultipartFile profileImage) {
        String imageUrl = s3Service.uploadImage(profileImage, "pet-profile-images");

        Pet savedPet = petRepository.save(req.toEntity(imageUrl));

        return PetResponse.fromEntity(savedPet);
    }

    //수정
    @Transactional
    public PetUpdateResponse PetProfileUpdate(long id, PetUpdateRequest req, MultipartFile profileImage) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new GlobalException(NOT_FOUND_PET_PROFILE));
        log.info("Profile Image in Service: {}", profileImage.getOriginalFilename());
        log.info("Profile Image Content Type: {}", profileImage.getContentType());
        log.info("Profile Image Size: {}", profileImage.getSize());
        if (profileImage != null && !profileImage.isEmpty()) {
            String newImageUrl = uploadProfileImage(profileImage);

            if (pet.getPhotoUrl() != null) {
                s3Service.deleteFile(pet.getPhotoUrl());
            }

            pet.update(req.getName(), req.getAge(), req.isNeutered(), newImageUrl);
        } else {
         pet.update(req.getName(), req.getAge(), req.isNeutered(), pet.getPhotoUrl());
        }

        return PetUpdateResponse.fromEntity(pet);
    }
    public String uploadProfileImage(MultipartFile profileImage) {
        return s3Service.uploadImage(profileImage, "pet-profile-images");
    }
    //삭제
    public void PetProfileDelete(long id) {
        petRepository.deleteById(id);
    }
}
