package teamyc.recordpet.global.image.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamyc.recordpet.global.BaseEntity;

@Getter
@Entity
@Table(name = "profile_image")
@NoArgsConstructor
public class ProfileImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private Type type;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "is_basic")
    private boolean isBasic;

    @Builder
    public ProfileImage(Long id, Type type, String imageUrl, boolean isBasic) {
        this.id = id;
        this.type = type;
        this.imageUrl = imageUrl;
        this.isBasic = isBasic;
    }
}
