package teamyc.recordpet.domain.weightLog.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import teamyc.recordpet.RecordPetApplication;
import teamyc.recordpet.domain.pet.entity.Gender;
import teamyc.recordpet.domain.pet.entity.Pet;
import teamyc.recordpet.domain.pet.repository.PetRepository;
import teamyc.recordpet.domain.weightLog.entity.WeightLog;
import teamyc.recordpet.domain.weightLog.repository.WeightLogRepository;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = RecordPetApplication.class)
class WeightLogControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private WeightLogRepository weightLogRepository;

    @Autowired
    private PetRepository petRepository;

    private MockMvc mockMvc;

    private Long testPetId;

    @BeforeEach
    void setUp() {
        weightLogRepository.deleteAll();
        petRepository.deleteAll();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Pet testPet = Pet.builder()
                .name("testPet")
                .age(3)
                .isNeutered(false)
                .gender(Gender.M)
                .photoUrl("프로필url부분")
                .build();
        petRepository.save(testPet);
        testPetId = testPet.getId();

        WeightLog testWeightLog = WeightLog.builder()
                .pet(testPet)
                .weight(2.5)
                .date(LocalDate.now())
                .build();
        weightLogRepository.save(testWeightLog);
    }

    @Test
    void RegisterWeightLog() throws Exception {
        String weightJson = """
                {
                    "petId": %d,
                    "weight": 2.5,
                    "date": "2025-01-15"
                }
                """.formatted(testPetId);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/weight")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(weightJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", notNullValue()))
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);
    }

    @Test
    void testGetWeights() throws Exception {
        String url = String.format("/api/v1/weight?petId=%d&startDate=%s&endDate=%s",
                testPetId, LocalDate.now().minusMonths(1), LocalDate.now());

        MvcResult mvcResult = mockMvc.perform(get(url)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", notNullValue())) // "data" 필드 확인
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].weight", is(2.5)))
                .andExpect(jsonPath("$.data[0].date", is(LocalDate.now().toString())))
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);
    }
}