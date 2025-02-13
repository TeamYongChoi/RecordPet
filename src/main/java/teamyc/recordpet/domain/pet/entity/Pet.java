package teamyc.recordpet.domain.pet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamyc.recordpet.domain.user.entity.User;
import teamyc.recordpet.global.BaseEntity;
import teamyc.recordpet.global.image.entity.ProfileImage;

@Entity
@Table(name = "pet")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Pet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "age", nullable = false)
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", columnDefinition = "ENUM('M','F')", nullable = false)
    private Gender gender;

    @Column(name = "is_neutered", nullable = false)
    private Boolean isNeutered;

    @ManyToOne
    @JoinColumn(name = "profile_image_id")
    private ProfileImage profileImage;

    @Builder
    public Pet(Long id, User user, String name, int age, Gender gender, Boolean isNeutered,
        ProfileImage profileImage) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.isNeutered = isNeutered;
        this.profileImage = profileImage;
    }


    public void update(String name, int age, Boolean isNeutered, ProfileImage image) {
        this.name = name;
        this.age = age;
        this.isNeutered = isNeutered;
        this.profileImage = image;
    }

    public String getProfileImageUrl() {
        return this.getProfileImage().getImageUrl();
    }
}
