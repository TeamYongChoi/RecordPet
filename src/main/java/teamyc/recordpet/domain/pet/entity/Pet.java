package teamyc.recordpet.domain.pet.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pet")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//TODO user 구현 완료 되는대로 주석 해제 할것.

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "age", nullable = false)
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", columnDefinition = "ENUM('M','F')", nullable = false)
    private Gender gender;

    @Column(name = "is_vaccinated", nullable = false)
    private Boolean isVaccinated;

    @Column(name = "photo_url", columnDefinition = "TEXT")
    private String photoUrl;

    @PrePersist
    public void prePersist() {
        if(this.photoUrl == null){
            this.photoUrl = "";
            //TODO 디폴트 이미지 추가하기
        }
    }

    @Builder
    public Pet(String name, int age, Gender gender, Boolean isVaccinated, String photoUrl) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.isVaccinated = isVaccinated;
        this.photoUrl = photoUrl;
    }

    public void update(String name, int age,Boolean isVaccinated, String photoUrl) {
        this.name = name;
        this.age = age;
        this.isVaccinated = isVaccinated;
        this.photoUrl = photoUrl;
    }
}
