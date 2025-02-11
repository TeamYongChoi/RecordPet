package teamyc.recordpet.domain.pet.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import teamyc.recordpet.domain.pet.dto.*;
import teamyc.recordpet.domain.pet.entity.Pet;
import teamyc.recordpet.domain.pet.repository.PetRepository;
import teamyc.recordpet.global.exception.GlobalException;
import teamyc.recordpet.global.image.ProfileImageRepository;
import teamyc.recordpet.global.image.entity.ProfileImage;
import teamyc.recordpet.global.image.entity.Type;
import teamyc.recordpet.global.s3.S3Service;

import java.util.List;
import java.util.stream.Collectors;

import static teamyc.recordpet.global.exception.ResultCode.BAD_REQUEST;
import static teamyc.recordpet.global.exception.ResultCode.NOT_FOUND_PET_PROFILE;

@RequiredArgsConstructor
@Service
@Slf4j
public class PetService {

    private final PetRepository petRepository;
    private final S3Service s3Service;
    private final ProfileImageRepository profileImageRepository;

    // user의 pet 전체 조회
    public List<GetPetProfileResponse> getAllPetProfile(Long userId) {
        return petRepository.findAllByUserId(userId)
                .orElseThrow(() -> new GlobalException(NOT_FOUND_PET_PROFILE))
                .stream()
                .map(GetPetProfileResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // user pet 상세 조회
    public GetPetDetailProfileResponse getPetDetailProfile(Long userId, Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new GlobalException(NOT_FOUND_PET_PROFILE));

        if (!userId.equals(pet.getUser().getId())) {
            throw new GlobalException(BAD_REQUEST);
        }

        return GetPetDetailProfileResponse.fromEntity(pet);
    }

    //등록
    public PetRegisterResponse savePetProfile(PetRegisterRequest req, MultipartFile profileImage) {
        // 사용자가 직접 프로필 업로드 하는 경우
        if (!profileImage.isEmpty()) {
            String profileImageUrl = s3Service.uploadImage(profileImage, "pet-profile-images");

            ProfileImage image = ProfileImage.builder()
                    .type(Type.PET)
                    .isBasic(false)
                    .imageUrl(profileImageUrl)
                    .build();

            profileImageRepository.save(image);
            petRepository.save(req.toEntity(image));

            return new PetRegisterResponse();

        }

        ProfileImage image = profileImageRepository.findBasicImage(Type.PET);

        petRepository.save(req.toEntity(image));

        return new PetRegisterResponse();
    }

    //수정
    @Transactional
    public PetUpdateResponse updatePetProfile(Long userId, Long petId, PetUpdateRequest req,
                                              MultipartFile profileImage) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new GlobalException(NOT_FOUND_PET_PROFILE));

        if (!userId.equals(pet.getUser().getId())) {
            throw new GlobalException(BAD_REQUEST);
        }

        log.info("Profile ProfileImage in Service: {}", profileImage.getOriginalFilename());
        log.info("Profile ProfileImage Content Type: {}", profileImage.getContentType());
        log.info("Profile ProfileImage Size: {}", profileImage.getSize());

        if (!profileImage.isEmpty()) {
            String newImageUrl = uploadProfileImage(profileImage);

            if (!pet.getProfileImage().isBasic()) {
                s3Service.deleteFile(pet.getProfileImageUrl());
                profileImageRepository.deleteById(pet.getProfileImage().getId());
            }

            ProfileImage image = ProfileImage.builder()
                    .type(Type.PET)
                    .isBasic(false)
                    .imageUrl(newImageUrl)
                    .build();

            ProfileImage savedImage = profileImageRepository.save(image);

            pet.update(req.getName(), req.getAge(), req.isNeutered(), savedImage);
        } else {
            pet.update(req.getName(), req.getAge(), req.isNeutered(), pet.getProfileImage());
        }

        return PetUpdateResponse.fromEntity(pet);
    }

    public String uploadProfileImage(MultipartFile profileImage) {
        return s3Service.uploadImage(profileImage, "pet-profile-images");
    }

    //삭제
    public void deletePetProfile(Long userId, Long petId) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new GlobalException(NOT_FOUND_PET_PROFILE));
        if (!userId.equals(pet.getUser().getId())) {
            throw new GlobalException(BAD_REQUEST);
        }
        petRepository.deleteById(petId);
    }
}
