package teamyc.recordpet.domain.pet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import teamyc.recordpet.RecordPetApplication;
import teamyc.recordpet.domain.pet.dto.PetRegisterRequest;
import teamyc.recordpet.domain.pet.dto.PetUpdateRequest;
import teamyc.recordpet.domain.pet.entity.Gender;
import teamyc.recordpet.domain.pet.entity.Pet;
import teamyc.recordpet.domain.pet.repository.PetRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = RecordPetApplication.class)
@AutoConfigureMockMvc(addFilters = false)
class PetControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private PetRepository petRepository;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void MockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        petRepository.deleteAll();
    }


    @DisplayName("펫 프로필 등록 성공")
    @Test
    public void addPetSuccess() throws Exception {
        //given
        final String url = "/api/v1/pets";
        final String name = "test";
        final int age = 3;
        final Gender gender = Gender.F;
        final Boolean isVaccinated = false;
        final String photoUrl = "testUrl";

        PetRegisterRequest req = new PetRegisterRequest(name, age, gender, isVaccinated, photoUrl);
        final String requestBody = objectMapper.writeValueAsString(req);
        //when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));
        //then
        result.andExpect(status().isCreated());

        List<Pet> pets = petRepository.findAll();

        assertThat(pets.size()).isEqualTo(1);
        assertThat(pets.get(0).getName()).isEqualTo(name);
        assertThat(pets.get(0).getAge()).isEqualTo(age);
        assertThat(pets.get(0).getGender()).isEqualTo(gender);
        assertThat(pets.get(0).getIsVaccinated()).isEqualTo(isVaccinated);
        assertThat(pets.get(0).getPhotoUrl()).isEqualTo(photoUrl);
    }

    @DisplayName("펫 프로필 수정 성공")
    @Test
    public void updatePetSuccess() throws Exception {
        //given
        final String url = "/api/v1/pets/{id}";
        Pet savedPet = createDefaultPet();

        final String newName = "test";
        final int newAge = 5;
        final Boolean newIsVaccinated = true;
        final String newPhotoUrl = "updateUrl";

        PetUpdateRequest request = PetUpdateRequest.builder()
                .name(newName)
                .age(newAge)
                .isVaccinated(newIsVaccinated)
                .photoUrl(newPhotoUrl)
                .build();
        //when
        ResultActions result = mockMvc.perform(put(url, savedPet.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isOk());

        Pet pet = petRepository.findById(savedPet.getId()).orElseThrow();

        assertThat(pet.getName()).isEqualTo(newName);
        assertThat(pet.getAge()).isEqualTo(newAge);
        assertThat(pet.getIsVaccinated()).isEqualTo(newIsVaccinated);
        assertThat(pet.getPhotoUrl()).isEqualTo(newPhotoUrl);
    }

    @DisplayName("펫 프로필 목록 조회 성공")
    @Test
    public void findAllPetsSuccess() throws Exception {
        //given
        final String url = "/api/v1/pets";
        Pet savedPet = createDefaultPet();
        //when
        final ResultActions result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON_VALUE));
        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(savedPet.getName()))
                .andExpect(jsonPath("$[0].age").value(savedPet.getAge()))
                .andExpect(jsonPath("$[0].gender").value(savedPet.getGender().toString()))
                .andExpect(jsonPath("$[0].isVaccinated").value(savedPet.getIsVaccinated().toString()))
                .andExpect(jsonPath("$[0].photoUrl").value(savedPet.getPhotoUrl()));
    }

    @DisplayName("펫 프로필 단건 조회 성공")
    @Test
    public void findPetSuccess() throws Exception {
        //given
        final String url = "/api/v1/pets/{id}";
        Pet savedPet = createDefaultPet();
        //when
        final ResultActions result = mockMvc.perform(get(url, savedPet.getId()));
        //then

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(savedPet.getName()))
                .andExpect(jsonPath("$.age").value(savedPet.getAge()))
                .andExpect(jsonPath("$.gender").value(savedPet.getGender().toString()))
                .andExpect(jsonPath("$.isVaccinated").value(savedPet.getIsVaccinated()))
                .andExpect(jsonPath("$.photoUrl").value(savedPet.getPhotoUrl()));
    }

    @DisplayName("펫 프로필 삭제 성공")
    @Test
    public void deletePetSuccess() throws Exception {
        //given
        final String url = "/api/v1/pets/{id}";
        Pet savedPet = createDefaultPet();

        //when
        mockMvc.perform(delete(url, savedPet.getId()))
                .andExpect(status().isOk());
        //then

        List<Pet> pets = petRepository.findAll();
        assertThat(pets).isEmpty();
    }

    private Pet createDefaultPet() {
        return petRepository.save(Pet.builder()
                .name("petName")
                .age(3)
                .gender(Gender.F)
                .isVaccinated(false)
                .photoUrl("sampleUrl")
                .build());
    }
}